package com.Teletubbies.Apollo.jwt.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class TokenInfo {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Date createAt;
    private Date updateAt;
}
