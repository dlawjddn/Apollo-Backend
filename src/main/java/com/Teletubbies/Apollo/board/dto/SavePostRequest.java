package com.Teletubbies.Apollo.board.dto;

import lombok.Data;

@Data
public class SavePostRequest {
    private Long userId;
    private String title;
    private String content;
}
