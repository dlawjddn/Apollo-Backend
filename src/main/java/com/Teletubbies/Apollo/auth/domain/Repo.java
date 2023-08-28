package com.Teletubbies.Apollo.auth.domain;

import com.Teletubbies.Apollo.auth.dto.RepoInfoResponse;
import com.Teletubbies.Apollo.deploy.domain.ApolloDeployService;
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
    private String repoName;
    private String repoUrl;
    private String ownerLogin;

    public Repo(RepoInfoResponse response) {
        this.id = response.getId();
        this.apolloUser = response.getApolloUser();
        this.ownerLogin = response.getUserLogin();
        this.repoName = response.getRepoName();
        this.repoUrl = response.getRepoURL();
    }
}
