package com.Teletubbies.Apollo.dto.comment.request;

import lombok.Data;

@Data
public class SaveCommentRequest {
    private Long userId;
    private Long postId;
    private String content;
}
