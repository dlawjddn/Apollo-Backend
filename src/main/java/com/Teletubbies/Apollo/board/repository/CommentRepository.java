package com.Teletubbies.Apollo.board.repository;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.board.domain.Comment;
import com.Teletubbies.Apollo.board.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findALlByPost(Post post);
    List<Comment> findAllByApolloUser(ApolloUser apolloUser);
}
