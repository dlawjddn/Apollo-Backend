package com.Teletubbies.Apollo.dto.post.response;

import com.Teletubbies.Apollo.dto.tag.ConvertTag;
import lombok.Data;

import java.util.List;
@Data
public class StartBoard {
    private Long count;
    private List<PostNoContentResponse> posts;
    private List<ConvertTag> tags;

    public StartBoard(Long count, List<PostNoContentResponse> posts, List<ConvertTag> tags) {
        this.count = count;
        this.posts = posts;
        this.tags = tags;
    }
}
