package com.Teletubbies.Apollo.dto.jwt;

import lombok.Data;

@Data
public class UserLoginRequest {
    private String userLogin;
    private String userId;
}
