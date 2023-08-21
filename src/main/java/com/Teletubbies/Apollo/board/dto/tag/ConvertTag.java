package com.Teletubbies.Apollo.board.dto.tag;

import lombok.Data;

@Data
public class ConvertTag {
    private Long tagId;
    private String tagName;

    public ConvertTag(Long tagId, String tagName) {
        this.tagId = tagId;
        this.tagName = tagName;
    }
}
