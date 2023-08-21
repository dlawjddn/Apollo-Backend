package com.Teletubbies.Apollo.deploy.repository;

import com.Teletubbies.Apollo.deploy.domain.ApolloDeployService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwsServiceRepository extends JpaRepository<ApolloDeployService, Long> {
    ApolloDeployService findByStackName(String stackName);
}
