package com.Teletubbies.Apollo.dto.comment.response;

import lombok.Data;

@Data
public class UpdateCommentResponse {
    private Long commentId;
    private String content;

    public UpdateCommentResponse(Long commentId, String content) {
        this.commentId = commentId;
        this.content = content;
    }
}
