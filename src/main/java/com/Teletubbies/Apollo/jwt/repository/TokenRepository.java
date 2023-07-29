package com.Teletubbies.Apollo.jwt.repository;

import com.Teletubbies.Apollo.jwt.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
}
