package com.Teletubbies.Apollo.dto.post.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdatePostRequest {
    private Long userId;
    private String title;
    private String content;
    private List<String> tagNames;
}
