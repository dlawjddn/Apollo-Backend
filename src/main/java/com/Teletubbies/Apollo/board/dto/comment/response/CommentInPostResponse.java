package com.Teletubbies.Apollo.board.dto.comment.response;

import lombok.Data;

import java.util.Date;

@Data
public class CommentInPostResponse {
    private Long commentId;
    private Long writerId;
    private String content;
    private Date writeAt;

    public CommentInPostResponse(Long commentId, Long writerId, String content, Date writeAt) {
        this.commentId = commentId;
        this.writerId = writerId;
        this.content = content;
        this.writeAt = writeAt;
    }
}
