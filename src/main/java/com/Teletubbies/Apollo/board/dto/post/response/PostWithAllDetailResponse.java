package com.Teletubbies.Apollo.board.dto.post.response;

import com.Teletubbies.Apollo.board.domain.Tag;
import com.Teletubbies.Apollo.board.dto.comment.response.CommentInPostResponse;
import com.Teletubbies.Apollo.board.dto.tag.ConvertTag;
import lombok.Data;

import java.util.List;

@Data
public class PostDetailResponse {
    private OriginPostResponse post;
    private List<CommentInPostResponse> comments;

    public PostDetailResponse(OriginPostResponse post, List<CommentInPostResponse> comments) {
        this.post = post;
        this.comments = comments;
    }
}
