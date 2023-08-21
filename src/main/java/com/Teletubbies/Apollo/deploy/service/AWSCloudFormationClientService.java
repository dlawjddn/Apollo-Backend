package com.Teletubbies.Apollo.deploy.service;

import com.Teletubbies.Apollo.auth.domain.Repo;
import com.Teletubbies.Apollo.auth.repository.RepoRepository;
import com.Teletubbies.Apollo.credential.domain.Credential;
import com.Teletubbies.Apollo.credential.exception.CredentialException;
import com.Teletubbies.Apollo.credential.repository.CredentialRepository;
import com.Teletubbies.Apollo.deploy.component.AwsClientComponent;
import com.Teletubbies.Apollo.deploy.domain.ApolloDeployService;
import com.Teletubbies.Apollo.deploy.repository.AwsServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.util.Optional;

import static com.Teletubbies.Apollo.core.exception.CustomErrorCode.CREDENTIAL_NOT_FOUND_ERROR;

@Service
@Slf4j
public class AWSCloudFormationClientService {
    private final CloudFormationClient cloudFormationClient;
    private final S3Client s3Client;
    private final RepoRepository repoRepository;
    private final CredentialRepository credentialRepository;
    private final AwsServiceRepository awsServiceRepository;

    public AWSCloudFormationClientService(AwsClientComponent awsClientComponent, RepoRepository repoRepository, CredentialRepository credentialRepository, AwsServiceRepository awsServiceRepository) {
        this.cloudFormationClient = awsClientComponent.createCFClient();
        this.s3Client = awsClientComponent.createS3Client();
        this.repoRepository = repoRepository;
        this.credentialRepository = credentialRepository;
        this.awsServiceRepository = awsServiceRepository;
    }

    @Transactional
    public String createClientStack(String repoName) {
        final String templateURL = "https://s3.amazonaws.com/apollo-script/client/cloudformation.yaml";
        try {
            Optional<Repo> repoInfoResponse = repoRepository.findByRepoName(repoName);

            if (repoInfoResponse.isPresent()) {
                Repo repo = repoInfoResponse.get();
                Long userId = repo.getApolloUser().getId();

                Credential credential = credentialRepository.findByApolloUserId(userId)
                        .orElseThrow(() -> new CredentialException(CREDENTIAL_NOT_FOUND_ERROR, "Credential 정보가 없습니다."));

                CreateStackRequest stackRequest = CreateStackRequest.builder()
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

                cloudFormationClient.createStack(stackRequest);
            }
        } catch (Exception e) {
            log.info("스택 생성중에 에러가 발생했습니다.: " + e.getMessage());
        }
        return null;
    }

    public void deleteClientStack(String stackName) {
        String bucketName = getBucketName(stackName);
        if (bucketName != null) {
            deleteS3Bucket(bucketName);
            deleteCloudFormationStack(stackName);
        } else {
            log.info("버킷이 존재하지 않습니다.");
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

    public void saveService(ApolloDeployService apolloDeployService) {
        awsServiceRepository.save(apolloDeployService);
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

    public String getS3BucketUrl(String stackName, String region) {
        try {
            DescribeStackResourcesRequest request = DescribeStackResourcesRequest.builder()
                    .stackName(stackName)
                    .build();
            DescribeStackResourcesResponse response = cloudFormationClient.describeStackResources(request);

            for (StackResource stackResource : response.stackResources()) {
                if ("AWS::S3::Bucket".equals(stackResource.resourceType())) {
                    String bucketName = stackResource.physicalResourceId();
                    return constructS3WebsiteEndpoint(bucketName, region);
                }
            }
        } catch (Exception e) {
            log.info("Error occurred while fetching S3 bucket for stack: " + e.getMessage());
        }
        return null;
    }

    private String constructS3WebsiteEndpoint(String bucketName, String region) {
        return "http://" + bucketName + ".s3-website." + region + ".amazonaws.com/";
    }
}
