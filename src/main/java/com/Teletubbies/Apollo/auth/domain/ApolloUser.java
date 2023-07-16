package com.Teletubbies.Apollo.auth.domain;

import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Table(name="user")
@Getter
public class ApolloUser {

    @Id
    private Long id; // userPassword에 해당하는 부분
    private String login; // username에 해당하는 부분
    private String name;
    private String profileUrl;
    @Column(nullable=true)
    private String email;

    public ApolloUser(MemberInfoResponse memberInfoResponse) {
        this.id = memberInfoResponse.getOauthId();
        this.login = memberInfoResponse.getLogin();
        this.name = memberInfoResponse.getUsername();
        this.email = memberInfoResponse.getEmail();
        this.profileUrl = memberInfoResponse.getProfileUrl();
    }
}
