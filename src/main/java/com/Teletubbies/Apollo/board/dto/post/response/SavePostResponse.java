package com.Teletubbies.Apollo.board.dto.post.response;

import lombok.Data;

@Data
public class SavePostResponse {
    private Long postId;
    private Long userId;

    public SavePostResponse(Long postId, Long userId) {
        this.postId = postId;
        this.userId = userId;
    }
}
