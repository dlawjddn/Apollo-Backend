package com.Teletubbies.Apollo.board.service;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.board.domain.Comment;
import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.dto.comment.request.SaveCommentRequest;
import com.Teletubbies.Apollo.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final PostService postService;
    @Transactional
    public Comment saveComment(SaveCommentRequest request){
        ApolloUser findUser = userService.getUserById(request.getUserId());
        Post findPost = postService.findPostById(request.getPostId());
        return commentRepository.save(new Comment(findUser, findPost, request.getContent()));
    }
    public List<Comment> findAllCommentByPost(Post post){
        return commentRepository.findALlByPost(post);
    }
    public List<Comment> findAllMyComments(Long userId){
        ApolloUser findUser = userService.getUserById(userId);
        return commentRepository.findAllByApolloUser(findUser);
    }
}
