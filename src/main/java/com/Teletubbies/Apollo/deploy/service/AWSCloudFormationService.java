package com.Teletubbies.Apollo.deploy.service;

import com.Teletubbies.Apollo.auth.repository.RepoRepository;
import com.Teletubbies.Apollo.credential.repository.CredentialRepository;
import com.Teletubbies.Apollo.deploy.component.AwsClientComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.*;

import java.util.List;

@Service
@Slf4j
public class AWSCloudFormationService {
    private final CloudFormationClient cloudFormationClient;

    public AWSCloudFormationService(AwsClientComponent awsClientComponent, RepoRepository repoRepository, CredentialRepository credentialRepository) {
        this.cloudFormationClient = awsClientComponent.createCFClient();
    }

    public void deleteStack(String stackName) {
        try {
            cloudFormationClient.deleteStack(builder -> builder.stackName(stackName));
            log.info("Delete stack: " + stackName + " successfully");
        } catch (Exception e) {
            log.error("Error occurred while deleting stack: " + e.getMessage());
        }
    }
}
