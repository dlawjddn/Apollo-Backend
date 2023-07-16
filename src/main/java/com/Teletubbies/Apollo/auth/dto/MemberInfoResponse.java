package com.Teletubbies.Apollo.auth.dto;

import com.Teletubbies.Apollo.auth.domain.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.beans.ConstructorProperties;

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

    @JsonProperty("avatar_url")
    private String profileUrl;

    public User changeDTOtoObj(MemberInfoResponse memberInfoResponse) {
        return new User(memberInfoResponse);
    }
}
