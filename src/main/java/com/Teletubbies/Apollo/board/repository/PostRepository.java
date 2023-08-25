package com.Teletubbies.Apollo.board.repository;

import com.Teletubbies.Apollo.board.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(Long id);
    Page<Post> findAll(Pageable pageable);
    Page<Post> findByContentContainingIgnoreCaseOrTitleContainingIgnoreCase(String title, String content, Pageable pageable);
    Long countByContentContainingIgnoreCaseOrTitleContainingIgnoreCase(String title, String content);
    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Long countByTitleContainingIgnoreCase(String title);
}
