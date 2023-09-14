package com.Teletubbies.Apollo.repository;

import com.Teletubbies.Apollo.domain.ApolloUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<ApolloUser, Long> {
    Optional<ApolloUser> findById(Long id);
    Optional<ApolloUser> findByLogin(String login);
    Optional<ApolloUser> findByName(String name);
}
