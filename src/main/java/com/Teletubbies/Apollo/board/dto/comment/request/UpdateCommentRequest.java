package com.Teletubbies.Apollo.board.dto.comment.request;

import lombok.Data;

@Data
public class UpdateCommentRequest {
    private Long userId;
    private Long commentId;
    private String content;
}
