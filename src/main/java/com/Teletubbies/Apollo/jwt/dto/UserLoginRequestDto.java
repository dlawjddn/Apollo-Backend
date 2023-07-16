package com.Teletubbies.Apollo.jwt.dto;

import lombok.Data;

@Data
public class UserLoginRequestDto {
    private String userLogin;
    private String userId;
}
