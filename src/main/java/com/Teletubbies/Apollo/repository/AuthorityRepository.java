package com.Teletubbies.Apollo.repository;

import com.Teletubbies.Apollo.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByAuthorityName(String authorityName);
}
