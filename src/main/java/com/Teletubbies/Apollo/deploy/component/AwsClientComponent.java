package com.Teletubbies.Apollo.deploy.component;

import com.Teletubbies.Apollo.deploy.dto.AwsServiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ecr.EcrClient;
import software.amazon.awssdk.services.s3.S3Client;

@Component
public class AwsClientComponent {
    private final AwsServiceDto awsServiceDto;

    @Autowired
    public AwsClientComponent(AwsServiceDto awsServiceDto) {
        this.awsServiceDto = awsServiceDto;
    }

    public Ec2Client createEc2Client() {
        AwsBasicCredentials awsCred = AwsBasicCredentials.create(awsServiceDto.getAccessKey(), awsServiceDto.getSecretKey());
        return Ec2Client.builder()
                .region(Region.of(awsServiceDto.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCred))
                .build();
    }

    public CloudFormationClient createCFClient() {
        AwsBasicCredentials awsCred = AwsBasicCredentials.create(awsServiceDto.getAccessKey(), awsServiceDto.getSecretKey());
        return CloudFormationClient.builder()
                .region(Region.of(awsServiceDto.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCred))
                .build();
    }

    public S3Client createS3Client() {
        AwsBasicCredentials awsCred = AwsBasicCredentials.create(awsServiceDto.getAccessKey(), awsServiceDto.getSecretKey());
        return S3Client.builder()
                .region(Region.of(awsServiceDto.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCred))
                .build();
    }

    public EcrClient createEcrClient() {
        AwsBasicCredentials awsCred = AwsBasicCredentials.create(awsServiceDto.getAccessKey(), awsServiceDto.getSecretKey());
        return EcrClient.builder()
                .region(Region.of(awsServiceDto.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCred))
                .build();
    }
}
