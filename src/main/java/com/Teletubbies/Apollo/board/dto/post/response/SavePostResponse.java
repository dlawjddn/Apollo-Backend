package com.Teletubbies.Apollo.board.dto.post.response;

import lombok.Data;

@Data
public class SavePostResponse {
    private Long postId;
    private Long userId;
    private String nickname;

    public SavePostResponse(Long postId, Long userId, String userLogin) {
        this.postId = postId;
        this.userId = userId;
        this.nickname = userLogin;
    }
}
