package com.Teletubbies.Apollo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenResponse {
    @JsonProperty("access_token")
    private String accessToken;
}
