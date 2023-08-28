package com.Teletubbies.Apollo.board.dto.comment.response;

import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.util.Date;

@Data
public class CommentInPostResponse {
    private Long commentId;
    private Long writerId;
    private String writerLogin;
    private String content;
    private Date writeAt;

    public CommentInPostResponse(Long commentId, Long writerId, String writerLogin, String content, Date writeAt) {
        this.commentId = commentId;
        this.writerId = writerId;
        this.writerLogin = writerLogin;
        this.content = content;
        this.writeAt = writeAt;
    }
}
