package com.Teletubbies.Apollo.deploy.component;

import com.Teletubbies.Apollo.deploy.dto.AwsServiceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;

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
}
