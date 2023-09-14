package com.Teletubbies.Apollo.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenRequest {
    private String client_id;
    private String client_secret;
    private String code;
}
