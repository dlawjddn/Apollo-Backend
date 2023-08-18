package com.Teletubbies.Apollo.board.dto.comment.request;

import lombok.Data;

@Data
public class DeleteCommentRequest {
    private Long userId;
    private Long commentId;
}
