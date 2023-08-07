package com.Teletubbies.Apollo.deploy.service;

import com.Teletubbies.Apollo.auth.domain.Repo;
import com.Teletubbies.Apollo.auth.repository.RepoRepository;
import com.Teletubbies.Apollo.core.exception.ApolloException;
import com.Teletubbies.Apollo.core.exception.CustomErrorCode;
import com.Teletubbies.Apollo.credential.domain.Credential;
import com.Teletubbies.Apollo.credential.repository.CredentialRepository;
import com.Teletubbies.Apollo.deploy.component.AwsClientComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.*;

import java.util.List;

import static com.Teletubbies.Apollo.core.exception.CustomErrorCode.NOT_FOUND_REPO_ERROR;

@Slf4j
@Service
public class AWSCloudFormationServerService {
    private final CloudFormationClient cloudFormationClient;
    private final RepoRepository repoRepository;
    private final CredentialRepository credentialRepository;

    public AWSCloudFormationServerService(AwsClientComponent awsClientComponent, RepoRepository repoRepository, CredentialRepository credentialRepository) {
        this.cloudFormationClient = awsClientComponent.createCFClient();
        this.repoRepository = repoRepository;
        this.credentialRepository = credentialRepository;
    }

    public void createServerStack(String repoName) {
        final String templateURL = "https://s3.amazonaws.com/apollo-script/api-build-test.yaml";
        try {
            Repo repo = repoRepository.findByRepoName(repoName)
                    .orElseThrow(() -> new ApolloException(NOT_FOUND_REPO_ERROR, "Repo 정보가 없습니다."));
            log.info("Repo 정보는 다음과 같습니다.");
            log.info("repo name: " + repo.getRepoName());
            log.info("repo url: " + repo.getRepoUrl());
            log.info("repo owner" + repo.getOwnerLogin());
            Long userId = repo.getApolloUser().getId();

            Credential credential = credentialRepository.findByApolloUserId(userId)
                    .orElseThrow(() -> new ApolloException(CustomErrorCode.CREDENTIAL_NOT_FOUND_ERROR, "Credential 정보가 없습니다."));

            log.info("Credential 정보는 다음과 같습니다.");
            log.info("Github Token: " + credential.getGithubOAuthToken());

            CreateStackRequest stackRequest = CreateStackRequest.builder()
                    .templateURL(templateURL)
                    .stackName(repoName)
                    .parameters(
                            Parameter.builder().parameterKey("AWSRegion").parameterValue(credential.getRegion()).build(),
                            Parameter.builder().parameterKey("AWSAccountId").parameterValue(credential.getAwsAccountId()).build(),
                            Parameter.builder().parameterKey("GithubRepositoryName").parameterValue(repo.getRepoName()).build(),
                            Parameter.builder().parameterKey("RepoLocation").parameterValue(repo.getRepoUrl()).build(),
                            Parameter.builder().parameterKey("RepoLogin").parameterValue(repo.getOwnerLogin()).build()
                    )
                    .capabilitiesWithStrings("CAPABILITY_IAM")
                    .build();

            cloudFormationClient.createStack(stackRequest);
            log.info("Create stack: " + repoName + " successfully");

            while (true) {
                DescribeStacksRequest describeRequest = DescribeStacksRequest.builder()
                        .stackName(repoName)
                        .build();
                DescribeStacksResponse describeResponse = cloudFormationClient.describeStacks(describeRequest);

                String stackStatus = describeResponse.stacks().get(0).stackStatusAsString();

                if (stackStatus.equals("CREATE_COMPLETE")) {
                    log.info("Create stack: " + repoName + " successfully");
                     break; // Break on success
                } else if (stackStatus.endsWith("FAILED") || stackStatus.equals("ROLLBACK_COMPLETE")) {
                    log.info("스택생성이 비정상적으로 종료되었습니다");
                    break; // Break on failure
                }
                // Wait for a bit before checking again
                Thread.sleep(10000); // 10 seconds
            }

        } catch (Exception e) {
            log.error("다음 이유로 스택 생성에 실패했습니다: " + e.getMessage());
        }
    }

    public void deleteServerStack(String stackName) {
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
        } catch (Exception e) {
            log.info("Error occurred while deleting stacks: " + e.getMessage());
        }
    }
}
