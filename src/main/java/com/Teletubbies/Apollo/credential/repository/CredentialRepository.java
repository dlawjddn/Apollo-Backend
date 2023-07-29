package com.Teletubbies.Apollo.credential.repository;

import com.Teletubbies.Apollo.credential.domain.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
}
