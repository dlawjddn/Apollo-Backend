package com.Teletubbies.Apollo.board.dto.post.response;

import lombok.Data;

import java.util.List;

@Data
public class PostSearchResponse {
    private Long count;
    private List<PostNoContentResponse> posts;

    public PostSearchResponse(Long count, List<PostNoContentResponse> posts) {
        this.count = count;
        this.posts = posts;
    }
}
