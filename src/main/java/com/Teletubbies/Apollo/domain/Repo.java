package com.Teletubbies.Apollo.domain;

import com.Teletubbies.Apollo.dto.auth.RepoInfoResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "repository")
public class Repo {
    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApolloUser apolloUser;
    @Column(name="repo_name")
    private String repoName;
    @Column(name="repo_url")
    private String repoUrl;
    @Column(name="owner_login")
    private String ownerLogin;

    public Repo(RepoInfoResponse response) {
        this.id = response.getId();
        this.apolloUser = response.getApolloUser();
        this.ownerLogin = response.getUserLogin();
        this.repoName = response.getRepoName();
        this.repoUrl = response.getRepoURL();
    }
}
