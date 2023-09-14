package com.Teletubbies.Apollo.dto.tag;

import com.Teletubbies.Apollo.domain.Tag;
import lombok.Data;

@Data
public class ConvertTag {
    private Long tagId;
    private String tagName;

    public ConvertTag(Long tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }
    public ConvertTag(Tag tag){
        this.tagId = tag.getId();
        this.tagName = tag.getName();
    }
}
