package com.Teletubbies.Apollo.deploy.service;

import com.Teletubbies.Apollo.auth.repository.RepoRepository;
import com.Teletubbies.Apollo.deploy.component.AwsClientComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.*;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.List;

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
                "https://s3.amazonaws.com/apollo-script/client-test.yaml";

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
