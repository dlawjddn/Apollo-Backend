package com.Teletubbies.Apollo.auth.dto;

import com.Teletubbies.Apollo.auth.domain.Repo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RepoInfoResponse {
    @JsonProperty("name")
    private String repoName;
    @JsonProperty("html_url")
    private String repoURL;
    public Repo changeDTOtoObj(RepoInfoResponse response){
        return new Repo(response);
    }
}
