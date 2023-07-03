package com.Teletubbies.Apollo.auth.domain;

import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Table(name="user")
@Getter
public class User {

    @Id
    private Long id;
    private String login;
    private String name;
    private String profileUrl;
    @Column(nullable=true)
    private String email;

    public User (MemberInfoResponse memberInfoResponse) {
        this.id = memberInfoResponse.getOauthId();
        this.login = memberInfoResponse.getLogin();
        this.name = memberInfoResponse.getUsername();
        this.email = memberInfoResponse.getEmail();
        this.profileUrl = memberInfoResponse.getProfileUrl();
    }
}
