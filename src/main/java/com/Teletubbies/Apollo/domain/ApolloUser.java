package com.Teletubbies.Apollo.domain;

import com.Teletubbies.Apollo.dto.auth.SaveUserRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Entity
@NoArgsConstructor
@Table(name="user")
@Getter
public class ApolloUser {

    @Id
    private Long id; // userPassword에 해당하는 부분
    private String login; // username에 해당하는 부분
    private String name;
    @Column(name="profile_url")
    private String profileUrl;
    @Column(nullable=true)
    private String email;
    @OneToMany(mappedBy = "apolloUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;
    @OneToMany(mappedBy = "apolloUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Repo> repos;
    @OneToOne(mappedBy = "apolloUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Credential credential;
    @OneToMany(mappedBy = "apolloUser", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ApolloDeployService> services;

    public ApolloUser(SaveUserRequest saveUserRequest) {
        this.id = saveUserRequest.getOauthId();
        this.login = saveUserRequest.getLogin();
        this.name = saveUserRequest.getUsername();
        this.email = saveUserRequest.getEmail();
        this.profileUrl = saveUserRequest.getProfileUrl();
    }
}
