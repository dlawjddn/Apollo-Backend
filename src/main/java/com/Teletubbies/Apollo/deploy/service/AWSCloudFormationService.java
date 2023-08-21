package com.Teletubbies.Apollo.deploy.service;

import com.Teletubbies.Apollo.deploy.component.AwsClientComponent;
import com.Teletubbies.Apollo.deploy.dto.request.GetDeployStackRequest;
import com.Teletubbies.Apollo.deploy.dto.response.GetDeployStackResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AWSCloudFormationService {
    private final AwsClientComponent awsClientComponent;

    public AWSCloudFormationService(AwsClientComponent awsClientComponent) {
        this.awsClientComponent = awsClientComponent;
    }

//    public List<GetDeployStackResponse> getDeployStacks(Long userId, GetDeployStackRequest request) {
//        String stackName = request.getStackName();
//    }
}
