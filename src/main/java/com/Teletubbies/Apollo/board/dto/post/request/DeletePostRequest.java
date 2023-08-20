package com.Teletubbies.Apollo.board.dto.post.request;

import lombok.Data;

@Data
public class DeletePostRequest {
    private Long userId;
    private Long postId;
}
