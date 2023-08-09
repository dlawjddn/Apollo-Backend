package com.Teletubbies.Apollo.jwt.repository;

import com.Teletubbies.Apollo.jwt.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByAuthorityName(String authorityName);
}
