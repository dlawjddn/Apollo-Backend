package com.Teletubbies.Apollo.auth.domain;

import com.Teletubbies.Apollo.auth.dto.RepoInfoResponse;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Repo {
    @Id
    @GeneratedValue
    private Long id;
    private String repoName;
    private String repoUrl;
    private String ownerLogin;
    public Repo(RepoInfoResponse response){
        this.ownerLogin = response.getUserLogin();
        this.repoName = response.getRepoName();
        this.repoUrl = response.getRepoURL();
    }
}
