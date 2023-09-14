package com.Teletubbies.Apollo.dto.comment.response;

import lombok.Data;

import java.util.Date;

@Data
public class SaveCommentResponse {
    private Long userId;
    private Long postId;
    private Long commentId;
    private String content;
    private Date createdAt;

    public SaveCommentResponse(Long userId, Long postId, Long commentId, String content, Date createdAt) {
        this.userId = userId;
        this.postId = postId;
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
    }
}
