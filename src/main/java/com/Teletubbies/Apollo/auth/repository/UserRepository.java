package com.Teletubbies.Apollo.auth.repository;

import com.Teletubbies.Apollo.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long id);
    Optional<User> findByLogin(String login);
    Optional<User> findByName(String name);
}
