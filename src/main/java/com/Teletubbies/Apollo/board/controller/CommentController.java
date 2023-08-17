package com.Teletubbies.Apollo.board.controller;

import com.Teletubbies.Apollo.board.domain.Comment;
import com.Teletubbies.Apollo.board.dto.comment.request.SaveCommentRequest;
import com.Teletubbies.Apollo.board.dto.comment.request.UpdateCommentRequest;
import com.Teletubbies.Apollo.board.dto.comment.response.MyCommentResponse;
import com.Teletubbies.Apollo.board.dto.comment.response.SaveCommentResponse;
import com.Teletubbies.Apollo.board.dto.comment.response.UpdateCommentResponse;
import com.Teletubbies.Apollo.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class CommentController {
    private final CommentService commentService;
    @PostMapping("/comment")
    public SaveCommentResponse saveComment(@RequestBody SaveCommentRequest request){
        Comment newComment = commentService.saveComment(request);
        return new SaveCommentResponse(
                newComment.getApolloUser().getId(),
                newComment.getPost().getId(),
                newComment.getId(),
                newComment.getContent(),
                newComment.getCreateAt());
    }
    @GetMapping("/comment/{userId}")
    public List<MyCommentResponse> findMyComments(@PathVariable Long userId){
         return commentService.findAllMyComments(userId).stream()
                .map(myComment -> new MyCommentResponse(
                        myComment.getPost().getId(),
                        myComment.getPost().getTitle(),
                        myComment.getContent(),
                        myComment.getUpdateAt()))
                .toList();
    }
    @PatchMapping("/comment")
    public UpdateCommentResponse updateComment(@RequestBody UpdateCommentRequest request){
        Comment updateComment = commentService.updateComment(request);
        log.info("댓글 수정 완료");
        return new UpdateCommentResponse(updateComment.getId(), updateComment.getContent());
    }
}
