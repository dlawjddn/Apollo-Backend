package com.Teletubbies.Apollo.repository;

import com.Teletubbies.Apollo.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, String> {
}
