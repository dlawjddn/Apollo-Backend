package com.Teletubbies.Apollo.board.repository;

import com.Teletubbies.Apollo.board.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
