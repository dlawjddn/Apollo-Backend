package com.Teletubbies.Apollo.board.dto.post.response;

import com.Teletubbies.Apollo.board.dto.comment.response.CommentInPostResponse;
import lombok.Data;

import java.util.List;

@Data
public class PostDetailResponse {
    private FindPostResponse post;
    private List<CommentInPostResponse> comments;

    public PostDetailResponse(FindPostResponse post, List<CommentInPostResponse> comments) {
        this.post = post;
        this.comments = comments;
    }
}
