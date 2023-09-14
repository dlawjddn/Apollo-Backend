package com.Teletubbies.Apollo.component;

import com.Teletubbies.Apollo.domain.Credential;
import com.Teletubbies.Apollo.repository.CredentialRepository;
import com.Teletubbies.Apollo.exception.DeploymentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cloudformation.CloudFormationClient;
import software.amazon.awssdk.services.cloudwatch.CloudWatchClient;
import software.amazon.awssdk.services.codepipeline.CodePipelineClient;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ecr.EcrClient;
import software.amazon.awssdk.services.elasticloadbalancingv2.ElasticLoadBalancingV2Client;
import software.amazon.awssdk.services.s3.S3Client;

import static com.Teletubbies.Apollo.exception.CustomErrorCode.*;

@Component
public class AwsClientComponent {
    private final CredentialRepository credentialRepository;

    @Autowired
    public AwsClientComponent(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public Ec2Client createEc2Client(Long userId) {
        Credential credential = credentialRepository.findByApolloUserId(userId)
                .orElseThrow(() -> new DeploymentException(NOT_FOUND_USER_ERROR, "해당 유저의 크리덴셜이 존재하지 않습니다."));
        AwsBasicCredentials awsCred = AwsBasicCredentials.create(credential.getAccessKey(), credential.getSecretKey());
        return Ec2Client.builder()
                .region(Region.of(credential.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCred))
                .build();
    }

    public CloudFormationClient createCFClient(Long userId) {
        Credential credential = credentialRepository.findByApolloUserId(userId)
                .orElseThrow(() -> new DeploymentException(NOT_FOUND_USER_ERROR, "해당 유저의 크리덴셜이 존재하지 않습니다."));
        AwsBasicCredentials awsCred = AwsBasicCredentials.create(credential.getAccessKey(), credential.getSecretKey());
        return CloudFormationClient.builder()
                .region(Region.of(credential.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCred))
                .build();
    }

    public S3Client createS3Client(Long userId) {
        Credential credential = credentialRepository.findByApolloUserId(userId)
                .orElseThrow(() -> new DeploymentException(NOT_FOUND_USER_ERROR, "해당 유저의 크리덴셜이 존재하지 않습니다."));
        AwsBasicCredentials awsCred = AwsBasicCredentials.create(credential.getAccessKey(), credential.getSecretKey());
        return S3Client.builder()
                .region(Region.of(credential.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCred))
                .build();
    }

    public EcrClient createEcrClient(Long userId) {
        Credential credential = credentialRepository.findByApolloUserId(userId)
                .orElseThrow(() -> new DeploymentException(NOT_FOUND_USER_ERROR, "해당 유저의 크리덴셜이 존재하지 않습니다."));
        AwsBasicCredentials awsCred = AwsBasicCredentials.create(credential.getAccessKey(), credential.getSecretKey());
        return EcrClient.builder()
                .region(Region.of(credential.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCred))
                .build();
    }

    public CloudWatchClient createCloudWatchClient(Long userId) {
        Credential credential = credentialRepository.findByApolloUserId(userId)
                .orElseThrow(() -> new DeploymentException(NOT_FOUND_USER_ERROR, "해당 유저의 크리덴셜이 존재하지 않습니다."));
        AwsBasicCredentials awsCred = AwsBasicCredentials.create(credential.getAccessKey(), credential.getSecretKey());
        return CloudWatchClient.builder()
                .region(Region.of(credential.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCred))
                .build();
    }

    public CodePipelineClient createCodePipelineClient(Long userId) {
        Credential credential = credentialRepository.findByApolloUserId(userId)
                .orElseThrow(() -> new DeploymentException(NOT_FOUND_USER_ERROR, "해당 유저의 크리덴셜이 존재하지 않습니다."));
        AwsBasicCredentials awsCred = AwsBasicCredentials.create(credential.getAccessKey(), credential.getSecretKey());
        return CodePipelineClient.builder()
                .region(Region.of(credential.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCred))
                .build();
    }

    public ElasticLoadBalancingV2Client createELBClient(Long userId) {
        Credential credential = credentialRepository.findByApolloUserId(userId)
                .orElseThrow(() -> new DeploymentException(NOT_FOUND_USER_ERROR, "해당 유저의 크리덴셜이 존재하지 않습니다."));
        AwsBasicCredentials awsCred = AwsBasicCredentials.create(credential.getAccessKey(), credential.getSecretKey());
        return ElasticLoadBalancingV2Client.builder()
                .region(Region.of(credential.getRegion()))
                .credentialsProvider(StaticCredentialsProvider.create(awsCred))
                .build();
    }
}
