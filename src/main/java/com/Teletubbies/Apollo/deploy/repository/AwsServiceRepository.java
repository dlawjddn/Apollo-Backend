package com.Teletubbies.Apollo.deploy.repository;

import com.Teletubbies.Apollo.deploy.domain.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AwsServiceRepository extends JpaRepository<Service, Long> {
}
