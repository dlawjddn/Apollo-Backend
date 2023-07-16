package com.Teletubbies.Apollo.deploy.service;

import com.Teletubbies.Apollo.deploy.dto.AwsServiceDto;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateVpcRequest;
import software.amazon.awssdk.services.ec2.model.CreateVpcResponse;
import software.amazon.awssdk.services.ec2.model.DeleteVpcRequest;
import software.amazon.awssdk.services.ec2.model.DeleteVpcResponse;

@Service
public class AwsVpcService {

    private final AwsServiceDto awsServiceDto;

    @Autowired
    public AwsVpcService(AwsServiceDto awsServiceDto) {
        this.awsServiceDto = awsServiceDto;
    }

    public Ec2Client createEc2Client() {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(awsServiceDto.getAccessKey(), awsServiceDto.getSecretKey());

        return Ec2Client.builder()
                .region(Region.AP_NORTHEAST_2)
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }

    public String createVpc() {

        Ec2Client ec2 = createEc2Client();

        CreateVpcRequest request = CreateVpcRequest.builder()
                .cidrBlock("10.2.0.0/16")
                .build();

        CreateVpcResponse response = ec2.createVpc(request);

        return response.vpc().vpcId();
    }

    public void deleteVpc(String vpcId) {
        Ec2Client ec2 = createEc2Client();

        DeleteVpcRequest request = DeleteVpcRequest.builder()
                .vpcId(vpcId)
                .build();

        DeleteVpcResponse response = ec2.deleteVpc(request);
    }
}
