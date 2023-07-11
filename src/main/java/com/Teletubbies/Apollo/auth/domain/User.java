package com.Teletubbies.Apollo.auth.domain;

import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Entity
@NoArgsConstructor
@Table(name="user")
@Getter
public class User{

    @Id
    private Long id;
    private String login; // username에 해당하는 부분
    private String name;
    private String profileUrl;
    @Column(nullable=true)
    private String email; // userPassword에 해당하는 부분

    public User (MemberInfoResponse memberInfoResponse) {
        this.id = memberInfoResponse.getOauthId();
        this.login = memberInfoResponse.getLogin();
        this.name = memberInfoResponse.getUsername();
        this.email = memberInfoResponse.getEmail();
        this.profileUrl = memberInfoResponse.getProfileUrl();
    }


}
