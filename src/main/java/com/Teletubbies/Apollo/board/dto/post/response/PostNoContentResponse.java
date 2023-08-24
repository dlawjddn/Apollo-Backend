package com.Teletubbies.Apollo.board.dto.post.response;

import com.Teletubbies.Apollo.board.dto.tag.ConvertTag;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class PostNoContentResponse {
    private Long userId;
    private Long postId;
    private String title;
    private List<ConvertTag> tags;
    private Date createAt;

    public PostNoContentResponse(Long userId, Long postId, String title, List<ConvertTag> tags, Date createAt) {
        this.userId = userId;
        this.postId = postId;
        this.title = title;
        this.tags = tags;
        this.createAt = createAt;
    }
}
