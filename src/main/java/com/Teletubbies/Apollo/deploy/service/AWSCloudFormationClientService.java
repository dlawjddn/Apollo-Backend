package com.Teletubbies.Apollo.deploy.service;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.domain.Repo;
import com.Teletubbies.Apollo.auth.repository.RepoRepository;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.credential.domain.Credential;
import com.Teletubbies.Apollo.credential.exception.CredentialException;
import com.Teletubbies.Apollo.credential.repository.CredentialRepository;
import com.Teletubbies.Apollo.deploy.component.AwsClientComponent;
import com.Teletubbies.Apollo.deploy.domain.ApolloDeployService;
import com.Teletubbies.Apollo.deploy.dto.request.PostClientDeployRequest;
import com.Teletubbies.Apollo.deploy.dto.response.PostClientDeployResponse;
import com.Teletubbies.Apollo.deploy.exception.DeploymentException;
import com.Teletubbies.Apollo.deploy.repository.AwsServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import static com.Teletubbies.Apollo.core.exception.CustomErrorCode.CREDENTIAL_NOT_FOUND_ERROR;
import static com.Teletubbies.Apollo.core.exception.CustomErrorCode.NOT_FOUND_REPO_ERROR;

@Service
@Slf4j
public class AWSCloudFormationClientService {
    private final CloudFormationClient cloudFormationClient;
    private final S3Client s3Client;
    private final RepoRepository repoRepository;
    private final CredentialRepository credentialRepository;
    private final AwsServiceRepository awsServiceRepository;
    private final UserService userService;

    public AWSCloudFormationClientService(AwsClientComponent awsClientComponent, RepoRepository repoRepository, CredentialRepository credentialRepository, AwsServiceRepository awsServiceRepository, UserService userService) {
        this.cloudFormationClient = awsClientComponent.createCFClient();
        this.s3Client = awsClientComponent.createS3Client();
        this.repoRepository = repoRepository;
        this.credentialRepository = credentialRepository;
        this.awsServiceRepository = awsServiceRepository;
        this.userService = userService;
    }

    @Transactional
    public PostClientDeployResponse saveService(Long userId, PostClientDeployRequest request) {
        String repoName = request.getRepoName();
        String EndPoint = createClientStack(repoName);
        ApolloUser user = userService.getUserById(userId);
        Repo repo = repoRepository.findByRepoName(repoName)
                .orElseThrow(() -> new DeploymentException(NOT_FOUND_REPO_ERROR, "해당 레포가 존재하지 않습니다."));
        ApolloDeployService apolloDeployService = new ApolloDeployService(user, repo, repoName, EndPoint, "client");
        awsServiceRepository.save(apolloDeployService);
        return new PostClientDeployResponse(repoName, "client", EndPoint);
    }

    public String createClientStack(String repoName) {
        final String templateURL = "https://s3.amazonaws.com/apollo-script/client/cloudformation.yaml";
        Repo repo = repoRepository.findByRepoName(repoName)
                .orElseThrow(() -> new DeploymentException(NOT_FOUND_REPO_ERROR, "해당 레포가 존재하지 않습니다."));
        Long userId = repo.getApolloUser().getId();

        Credential credential = credentialRepository.findByApolloUserId(userId)
                .orElseThrow(() -> new CredentialException(CREDENTIAL_NOT_FOUND_ERROR, "Credential 정보가 없습니다."));

        CreateStackRequest stackRequest = getCreateStackRequest(repoName, templateURL, repo, credential);
        cloudFormationClient.createStack(stackRequest);
        Output output = getOutput(repoName);
        return output.outputValue();
    }

    private Output getOutput(String repoName) {
        DescribeStacksRequest describeStacksRequest = DescribeStacksRequest.builder().stackName(repoName).build();
        cloudFormationClient.waiter().waitUntilStackCreateComplete(describeStacksRequest);
        DescribeStacksResponse describeStacksResponse = cloudFormationClient.describeStacks(describeStacksRequest);
        return describeStacksResponse.stacks().get(0).outputs().get(0);
    }

    private CreateStackRequest getCreateStackRequest(String repoName, String templateURL, Repo repo, Credential credential) {
        return CreateStackRequest
                .builder()
                .templateURL(templateURL)
                .stackName(repoName)
                .parameters(
                        Parameter.builder().parameterKey("RepoName").parameterValue(repo.getRepoName()).build(),
                        Parameter.builder().parameterKey("UserLogin").parameterValue(repo.getOwnerLogin()).build(),
                        Parameter.builder().parameterKey("RepoLocation").parameterValue(repo.getRepoUrl()).build(),
                        Parameter.builder().parameterKey("GithubToken").parameterValue(credential.getGithubOAuthToken()).build(),
                        Parameter.builder().parameterKey("region").parameterValue(credential.getRegion()).build()
                )
                .capabilitiesWithStrings("CAPABILITY_IAM")
                .build();
    }

    public void deleteClientStack(Long userId, String stackName) {
        ApolloUser user = userService.getUserById(userId);
        String bucketName = getBucketName(stackName);
        if (bucketName != null) {
            deleteS3Bucket(bucketName);
            deleteCloudFormationStack(stackName);
        } else {
            log.info("버킷이 존재하지 않습니다.");
        }
        ApolloDeployService apolloDeployService = awsServiceRepository.findByApolloUserAndStackName(user, stackName);
        if (apolloDeployService != null) {
            awsServiceRepository.delete(apolloDeployService);
        }
    }

    public void deleteS3Bucket(String bucketName) {
        try {
            emptyS3Bucket(bucketName);
            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder().bucket(bucketName).build();
            s3Client.deleteBucket(deleteBucketRequest);
            log.info("Delete bucket: " + bucketName + " successfully");
        } catch (Exception e) {
            log.info("버킷 삭제중에 에러가 발생했습니다.: " + e.getMessage());
        }
    }

    public void emptyS3Bucket(String bucketName) {
        ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder().bucket(bucketName).build();
        ListObjectsV2Response listObjectsV2Response;

        do {
            listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);
            for (S3Object s3Object : listObjectsV2Response.contents()) {
                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder().bucket(bucketName).key(s3Object.key()).build();
                s3Client.deleteObject(deleteObjectRequest);
            }
            listObjectsV2Request = listObjectsV2Request.toBuilder().continuationToken(listObjectsV2Response.nextContinuationToken()).build();
        } while (listObjectsV2Response.isTruncated());
    }

    public void deleteCloudFormationStack(String stackName) {
        try {
            DeleteStackRequest deleteStackRequest = DeleteStackRequest.builder().stackName(stackName).build();
            cloudFormationClient.deleteStack(deleteStackRequest);
            log.info("Delete stack: " + stackName + " successfully");
        } catch (Exception e) {
            log.info("스택 삭제중에 에러가 발생했습니다.: " + e.getMessage());
        }
    }

    public String getBucketName(String stackName) {
        try {
            DescribeStackResourcesRequest request = DescribeStackResourcesRequest.builder()
                    .stackName(stackName)
                    .build();
            DescribeStackResourcesResponse response = cloudFormationClient.describeStackResources(request);
            for (StackResource stackResource : response.stackResources()) {
                if ("AWS::S3::Bucket".equals(stackResource.resourceType())) {
                    return stackResource.physicalResourceId();
                }
            }
        } catch (Exception e) {
            log.info("Error occurred while fetching S3 bucket for stack: " + e.getMessage());
        }
        return null;
    }
}
