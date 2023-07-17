package com.Teletubbies.Apollo.deploy.service;

import com.Teletubbies.Apollo.deploy.component.AwsClientComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.CreateSubnetRequest;
import software.amazon.awssdk.services.ec2.model.CreateSubnetResponse;
import software.amazon.awssdk.services.ec2.model.DeleteSubnetRequest;
import software.amazon.awssdk.services.ec2.model.DeleteSubnetResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class AwsSubnetService {

    private final AwsClientComponent awsClientComponent;

    public AwsSubnetService(AwsClientComponent awsClientComponent) {
        this.awsClientComponent = awsClientComponent;
    }

    public Ec2Client createEc2Client() {
        return awsClientComponent.createEc2Client();
    }

    List<String> cidrBlocks = Arrays.asList(
            "10.2.1.0/24", // public subnet A
            "10.2.2.0/24", // public subnet C
            "10.2.3.0/24", // private subnet A
            "10.2.4.0/24", // private subnet C
            "10.2.5.0/24", // db subnet A
            "10.2.6.0/24" // db subnet C
    );

    List<String> availabilityZones = Arrays.asList(
            "ap-northeast-2a",
            "ap-northeast-2c"
    );

    public String createSubnet(String vpcId, String cidrBlock, String availabilityZone) {
        Ec2Client ec2 = createEc2Client();

        CreateSubnetRequest request = CreateSubnetRequest.builder()
                .vpcId(vpcId)
                .cidrBlock(cidrBlock)
                .availabilityZone(availabilityZone)
                .build();

        CreateSubnetResponse response = ec2.createSubnet(request);
        String subnetId = response.subnet().subnetId();
        log.info("Create Subnet ID: " + subnetId);
        return subnetId;
    }

    public void deleteSubnet(String subnetId) {
        Ec2Client ec2 = createEc2Client();

        DeleteSubnetRequest request = DeleteSubnetRequest.builder()
                .subnetId(subnetId)
                .build();
        log.info("Delete Subnet ID: " + subnetId);
        DeleteSubnetResponse response = ec2.deleteSubnet(request);
    }

    public String createSubnets(String vpcId) {

        for (int i = 0; i < cidrBlocks.size(); i++) {
            String subnetId = createSubnet(vpcId, cidrBlocks.get(i), availabilityZones.get(i % 2));
            log.info("Created Subnet: " + subnetId);
        }
        return "ok";
    }
}
