package com.Teletubbies.Apollo.repository;

import com.Teletubbies.Apollo.domain.ApolloUser;
import com.Teletubbies.Apollo.domain.Comment;
import com.Teletubbies.Apollo.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<Comment> findById(Long aLong);
    List<Comment> findALlByPost(Post post);
    List<Comment> findAllByApolloUser(ApolloUser apolloUser);
}
