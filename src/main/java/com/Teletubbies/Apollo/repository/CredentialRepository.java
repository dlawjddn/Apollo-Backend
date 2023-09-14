package com.Teletubbies.Apollo.repository;

import com.Teletubbies.Apollo.domain.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
    Optional<Credential> findByApolloUserId(Long userId);
}
