package com.Teletubbies.Apollo.board.dto.post.response;

import com.Teletubbies.Apollo.board.dto.tag.ConvertTag;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostOnlyPostResponse {
    private Long userId;
    private String userLogin;
    private Long postId;
    private String title;
    private String content;
    private List<ConvertTag> tags;
    private Date createAt;

    public PostOnlyPostResponse(Long userId, String userLogin, Long postId, String title, String content, List<ConvertTag> tags, Date createAt) {
        this.userId = userId;
        this.userLogin = userLogin;
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.tags = tags;
        this.createAt = createAt;
    }
}
