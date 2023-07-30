package com.Teletubbies.Apollo.deploy.service;

import com.Teletubbies.Apollo.deploy.component.AwsClientComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.CloudFormationException;
import software.amazon.awssdk.services.cloudformation.model.DescribeStacksResponse;
import software.amazon.awssdk.services.cloudformation.model.Stack;

import java.util.List;

@Service
@Slf4j
public class AWSCloudFormationService {
    private final CloudFormationClient cloudFormationClient;

    public AWSCloudFormationService(AwsClientComponent awsClientComponent) {
        this.cloudFormationClient = awsClientComponent.createCFClient();
    }

    public void createStack(String stackName, String type) {
        String templateURL = type.equals("api") ?
            "https://s3.amazonaws.com/apollo-script/Apollo-Script/cloudformation/api.yaml" :
            "https://s3.amazonaws.com/apollo-script/Apollo-Script/cloudformation/client.yaml";

        try {
            cloudFormationClient.createStack(builder -> builder.stackName(stackName).templateURL(templateURL));
            log.info("Create stack: " + stackName + " successfully");
        } catch (Exception e) {
            log.error("Error occurred while creating stack: " + e.getMessage());
        }
    }

    public void deleteStack(String stackName) {
        try {
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
}
