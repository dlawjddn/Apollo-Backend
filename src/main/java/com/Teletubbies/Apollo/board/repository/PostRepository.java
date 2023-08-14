package com.Teletubbies.Apollo.board.repository;

import com.Teletubbies.Apollo.board.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
}
