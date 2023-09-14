package com.Teletubbies.Apollo.dto.post.response;

import lombok.Data;

@Data
public class SavePostResponse {
    private Long postId;
    private Long userId;
    private String userLogin;

    public SavePostResponse(Long postId, Long userId, String userLogin) {
        this.postId = postId;
        this.userId = userId;
        this.userLogin = userLogin;
    }
}
