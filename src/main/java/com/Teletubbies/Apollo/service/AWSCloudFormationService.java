package com.Teletubbies.Apollo.service;

import com.Teletubbies.Apollo.component.AwsClientComponent;
import com.Teletubbies.Apollo.domain.ApolloDeployService;
import com.Teletubbies.Apollo.dto.deploy.response.GetDeployStackResponse;
import com.Teletubbies.Apollo.repository.AwsServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudformation.model.DescribeStackResourcesRequest;
import software.amazon.awssdk.services.cloudformation.model.StackResource;
import software.amazon.awssdk.services.codepipeline.CodePipelineClient;
import software.amazon.awssdk.services.codepipeline.model.GetPipelineStateRequest;
import software.amazon.awssdk.services.codepipeline.model.StageExecution;
import software.amazon.awssdk.services.codepipeline.model.StageState;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AWSCloudFormationService {
    private final AwsClientComponent awsClientComponent;
    private final AwsServiceRepository awsServiceRepository;

    public AWSCloudFormationService(AwsClientComponent awsClientComponent, AwsServiceRepository awsServiceRepository) {
        this.awsClientComponent = awsClientComponent;
        this.awsServiceRepository = awsServiceRepository;
    }

    @Transactional
    public List<GetDeployStackResponse> getDeployStacks(Long userId) {
        List<GetDeployStackResponse> responses = new ArrayList<>();
        List<ApolloDeployService> services = awsServiceRepository.findByApolloUserId(userId);
        for (ApolloDeployService service: services) {
            GetDeployStackResponse response;
            if (checkCodePipeline(service.getStackName(), userId)) {
                response = new GetDeployStackResponse(service.getId(), service.getStackName(), service.getStackType(), service.getEndpoint(), "success");
            } else {
                response = new GetDeployStackResponse(service.getId(), service.getStackName(), service.getStackType(), service.getEndpoint(), "deploying");
            }
            responses.add(response);
        }
        return responses;
    }

    public boolean checkCodePipeline(String stackName, Long userId) {
        CloudFormationClient cfClient = awsClientComponent.createCFClient(userId);
        CodePipelineClient cpClient = awsClientComponent.createCodePipelineClient(userId);

        DescribeStackResourcesRequest request = DescribeStackResourcesRequest.builder().stackName(stackName).build();
        String pipelineName = cfClient.describeStackResources(request).stackResources().stream()
                .filter(resource -> "AWS::CodePipeline::Pipeline".equals(resource.resourceType()))
                .map(StackResource::physicalResourceId)
                .findFirst()
                .orElse(null);

        if (pipelineName == null) {
            return false;
        }

        GetPipelineStateRequest getPipelineStateRequest = GetPipelineStateRequest.builder().name(pipelineName).build();
        List<StageState> stageStates = cpClient.getPipelineState(getPipelineStateRequest).stageStates();

        for (StageState stageState : stageStates) {
            StageExecution latestExecution = stageState.latestExecution();
            if (latestExecution == null || !"Succeeded".equals(latestExecution.status().toString())) {
                return false;
            }
        }
        return true;
    }
}
