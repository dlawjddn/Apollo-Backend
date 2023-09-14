package com.Teletubbies.Apollo.repository;

import com.Teletubbies.Apollo.domain.ApolloUser;
import com.Teletubbies.Apollo.domain.ApolloDeployService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AwsServiceRepository extends JpaRepository<ApolloDeployService, Long> {
    ApolloDeployService findByApolloUserAndStackName(ApolloUser user, String stackName);
    List<ApolloDeployService> findByApolloUserId(Long userId);
}
