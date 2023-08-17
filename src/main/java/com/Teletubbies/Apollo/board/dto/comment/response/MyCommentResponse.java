package com.Teletubbies.Apollo.board.dto.comment.response;

import lombok.Data;

import java.util.Date;

@Data
public class MyCommentResponse {
    private Long postId;
    private String postTitle;
    private String content;
    private Date createAt;

    public MyCommentResponse(Long postId, String postTitle, String content, Date createAt) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.content = content;
        this.createAt = createAt;
    }
}
