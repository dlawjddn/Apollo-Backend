package com.Teletubbies.Apollo.deploy.service;

import com.Teletubbies.Apollo.auth.domain.Repo;
import com.Teletubbies.Apollo.auth.repository.RepoRepository;
import com.Teletubbies.Apollo.deploy.component.AwsClientComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.*;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteBucketRequest;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AWSCloudFormationService {
    private final CloudFormationClient cloudFormationClient;
    private final S3Client s3Client;
    private final RepoRepository repoRepository;

    public AWSCloudFormationService(AwsClientComponent awsClientComponent, RepoRepository repoRepository) {
        this.cloudFormationClient = awsClientComponent.createCFClient();
        this.s3Client = awsClientComponent.createS3Client();
        this.repoRepository = repoRepository;
    }

    public void createStack(String repoName, String stackName, String type) {
        String templateURL = type.equals("api") ?
            "https://s3.amazonaws.com/apollo-script/Apollo-Script/cloudformation/api.yaml" :
            //"https://s3.amazonaws.com/apollo-script/Apollo-Script/cloudformation/client.yaml";
                "https://s3.amazonaws.com/apollo-script/client-build-test.yaml";

        repoRepository.findByRepoName(repoName);

        CreateStackRequest stackRequest = CreateStackRequest.builder()
                .templateURL(templateURL)
                .stackName(stackName)
                .build();

        try {
            cloudFormationClient.createStack(stackRequest);
            log.info("Create stack: " + stackName + " successfully");
        } catch (Exception e) {
            log.error("Error occurred while creating stack: " + e.getMessage());
        }
    }

    public void createClientStack(String repoName) {
        String templateURL = "https://s3.amazonaws.com/apollo-script/client-build-test.yaml";
        try {
            Optional<Repo> repoInfoResponse = repoRepository.findByRepoName(repoName);

            if (repoInfoResponse.isPresent()) {
                Repo repo = repoInfoResponse.get();
                log.info("Repo 정보는 다음과 같습니다: " + repo.getRepoName());
                log.info("repo url: " + repo.getRepoUrl());
                log.info("repo owner login: " + repo.getOwnerLogin());

                CreateStackRequest stackRequest = CreateStackRequest.builder()
                        .templateURL(templateURL)
                        .stackName(repoName)
                        .parameters(
                                Parameter.builder().parameterKey("RepoName").parameterValue(repo.getRepoName()).build(),
                                Parameter.builder().parameterKey("UserLogin").parameterValue(repo.getOwnerLogin()).build(),
                                Parameter.builder().parameterKey("RepoLocation").parameterValue(repo.getRepoUrl()).build(),
                                Parameter.builder().parameterKey("GithubToken").parameterValue("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").build()
                        )
                        .capabilitiesWithStrings("CAPABILITY_IAM")
                        .build();

                cloudFormationClient.createStack(stackRequest);

                while (true) {
                    DescribeStacksRequest describeRequest = DescribeStacksRequest.builder()
                            .stackName(repoName)
                            .build();
                    DescribeStacksResponse describeResponse = cloudFormationClient.describeStacks(describeRequest);

                    String stackStatus = describeResponse.stacks().get(0).stackStatusAsString();

                    if (stackStatus.equals("CREATE_COMPLETE")) {
                        log.info("Create stack: " + repoName + " successfully");
                        break;
                    } else if (stackStatus.endsWith("FAILED") || stackStatus.equals("ROLLBACK_COMPLETE")) {
                        log.info("스택생성이 비정상적으로 종료되었습니다");
                        break; // Break on failure
                    }
                    // Wait for a bit before checking again
                    Thread.sleep(10000); // 10 seconds
                }
            }
        } catch (Exception e) {
            log.info("스택 생성중에 에러가 발생했습니다.: " + e.getMessage());
        }
    }

    public void createApiStack(String repoName) {
        String templateURL = "https://s3.amazonaws.com/apollo-script/Apollo-Script/cloudformation/api.yaml";
    }

    public void deleteStack(String stackName) {
        try {
//            String bucketName = getBucketName(stackName);
//            ListObjectsV2Request listReq = ListObjectsV2Request.builder()
//                    .bucket(bucketName)
//                    .build();
//            ListObjectsV2Response listRes = s3Client.listObjectsV2(listReq);
//
//            for (software.amazon.awssdk.services.s3.model.S3Object s3Object : listRes.contents()) {
//                s3Client.deleteObject(builder -> builder.bucket(bucketName).key(s3Object.key()));
//            }
//            DeleteBucketRequest deleteBucketRequest = DeleteBucketRequest.builder()
//                    .bucket(bucketName)
//                    .build();
//            s3Client.deleteBucket(deleteBucketRequest);
            cloudFormationClient.deleteStack(builder -> builder.stackName(stackName));
            log.info("Delete stack: " + stackName + " successfully");
        } catch (Exception e) {
            log.error("Error occurred while deleting stack: " + e.getMessage());
        }
    }

    public void describeStacks() {
        try {
            DescribeStacksResponse describeStacksResponse = cloudFormationClient.describeStacks();
            List<Stack> stacks = describeStacksResponse.stacks();
            for (Stack stack : stacks) {
                log.info("Stack name: " + stack.stackName());
                log.info("Stack status: " + stack.stackStatusAsString());
            }
            log.info("Describe stacks successfully");
        } catch (CloudFormationException e) {
            log.error("Error occurred while describing stacks: " + e.getMessage());
        }
    }

    public String getBucketName(String stackName) {
        DescribeStacksRequest describeStacksRequest = DescribeStacksRequest.builder()
                .stackName(stackName)
                .build();
        DescribeStacksResponse describeStackResponse = cloudFormationClient.describeStacks(describeStacksRequest);

        Stack stack = describeStackResponse.stacks().get(0);

        for (Output output: stack.outputs()) {
            log.info(output.outputKey() + " : " + output.outputValue());
            if (output.outputKey().equals("no-duplicated-bucket-name-with-apollo")) {
                return output.outputValue();
            }
        }
        throw new RuntimeException("Bucket name not found in stack: " + stackName);
    }
}
