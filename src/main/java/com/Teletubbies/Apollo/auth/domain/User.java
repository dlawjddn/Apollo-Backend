package com.Teletubbies.Apollo.auth.domain;

import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class User {

    @Id
    private Long id;

    private String login;
    private String name;

    @Column(nullable=true)
    private String email;

    public User (MemberInfoResponse memberInfoResponse) {
        this.id = memberInfoResponse.getOauthId();
        this.login = memberInfoResponse.getLogin();
        this.name = memberInfoResponse.getUsername();
        this.email = memberInfoResponse.getEmail();
    }
}
