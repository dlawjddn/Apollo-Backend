package com.Teletubbies.Apollo.dto.post.response;

import lombok.Data;

@Data
public class UpdatePostResponse {
    private Long postId;
    public UpdatePostResponse(Long postId) {
        this.postId = postId;
    }
}
