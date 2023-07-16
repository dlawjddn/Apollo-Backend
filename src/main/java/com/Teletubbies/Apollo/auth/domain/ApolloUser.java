package com.Teletubbies.Apollo.auth.domain;

import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;


@Entity
@NoArgsConstructor
@Table(name="user")
@Getter
public class ApolloUser implements UserDetails{

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.id.toString();
    }

    @Override
    public String getUsername() {
        return this.login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
