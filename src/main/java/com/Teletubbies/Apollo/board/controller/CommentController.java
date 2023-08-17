package com.Teletubbies.Apollo.board.controller;

import com.Teletubbies.Apollo.board.domain.Comment;
import com.Teletubbies.Apollo.board.dto.comment.request.SaveCommentRequest;
import com.Teletubbies.Apollo.board.dto.comment.response.SaveCommentResponse;
import com.Teletubbies.Apollo.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/comment")
    public SaveCommentResponse saveComment(@RequestBody SaveCommentRequest request){
        Comment newComment = commentService.saveComment(request);
        return new SaveCommentResponse(newComment.getApolloUser().getId(), newComment.getPost().getId(), newComment.getId(), newComment.getContent(), newComment.getCreateAt());
    }
}
