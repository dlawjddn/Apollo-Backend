package com.Teletubbies.Apollo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberInfoResponse {
    @JsonProperty("id")
    private Long oauthId;

    @JsonProperty("login")
    private String login;

    @JsonProperty("name")
    private String username;

    @JsonProperty("email")
    private String email;
}
