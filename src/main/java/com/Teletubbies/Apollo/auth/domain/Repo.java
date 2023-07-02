package com.Teletubbies.Apollo.auth.domain;

import com.Teletubbies.Apollo.auth.dto.RepoInfoResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Repo {
    @Id
    @GeneratedValue
    private Long id;
    private String repoName;
    private String repoUrl;
    public Repo(RepoInfoResponse response){
        this.repoName = response.getRepoName();
        this.repoUrl = response.getRepoURL();
    }
}
