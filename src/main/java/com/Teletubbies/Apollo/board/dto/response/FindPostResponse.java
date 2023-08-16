package com.Teletubbies.Apollo.board.dto.response;

import lombok.Data;

@Data
public class FindPostResponse {
    private Long userId;
    private Long postId;
    private String title;
    private String content;

    public FindPostResponse(Long userId, Long postId, String title, String content) {
        this.userId = userId;
        this.postId = postId;
        this.title = title;
        this.content = content;
    }
}
