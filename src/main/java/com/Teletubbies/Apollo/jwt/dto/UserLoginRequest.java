package com.Teletubbies.Apollo.jwt.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String userLogin;
    private String userId;
}
