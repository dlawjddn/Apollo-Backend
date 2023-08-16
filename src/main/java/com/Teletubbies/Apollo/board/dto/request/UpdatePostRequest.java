package com.Teletubbies.Apollo.board.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class UpdatePostRequest {
    private Long userId;
    private Long postId;
    private String title;
    private String content;
    private List<String> tagNames;
}
