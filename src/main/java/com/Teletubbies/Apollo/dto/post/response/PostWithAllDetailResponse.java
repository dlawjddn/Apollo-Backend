package com.Teletubbies.Apollo.dto.post.response;

import com.Teletubbies.Apollo.dto.comment.response.CommentInPostResponse;
import lombok.Data;

import java.util.List;

@Data
public class PostWithAllDetailResponse {
    private PostOnlyPostResponse post;
    private List<CommentInPostResponse> comments;

    public PostWithAllDetailResponse(PostOnlyPostResponse post, List<CommentInPostResponse> comments) {
        this.post = post;
        this.comments = comments;
    }
}
