package com.Teletubbies.Apollo.board.dto;

import lombok.Data;

import java.util.List;

@Data
public class SavePostRequest {
    private Long userId;
    private String title;
    private String content;
    private List<String> tagNames;
}
