package com.Teletubbies.Apollo.deploy.service;

import com.Teletubbies.Apollo.deploy.component.AwsClientComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateVpcRequest;
import software.amazon.awssdk.services.ec2.model.CreateVpcResponse;
import software.amazon.awssdk.services.ec2.model.DeleteVpcRequest;
import software.amazon.awssdk.services.ec2.model.DeleteVpcResponse;

@Service
@Slf4j
public class AwsVpcService {

    private final AwsClientComponent awsClientComponent;

    @Autowired
    public AwsVpcService(AwsClientComponent awsClientComponent) {
        this.awsClientComponent = awsClientComponent;
    }

    public Ec2Client createEc2Client() {
        return awsClientComponent.createEc2Client();
    }

    public String createVpc() {

        Ec2Client ec2 = createEc2Client();

        CreateVpcRequest request = CreateVpcRequest.builder()
                .cidrBlock("10.2.0.0/16")
                .build();

        CreateVpcResponse response = ec2.createVpc(request);
        log.info("Create VPC ID: " + response.vpc().vpcId());
        return response.vpc().vpcId();
    }

    public void deleteVpc(String vpcId) {
        Ec2Client ec2 = createEc2Client();

        DeleteVpcRequest request = DeleteVpcRequest.builder()
                .vpcId(vpcId)
                .build();
        log.info("Delete VPC ID: " + vpcId);
        DeleteVpcResponse response = ec2.deleteVpc(request);
    }
}
