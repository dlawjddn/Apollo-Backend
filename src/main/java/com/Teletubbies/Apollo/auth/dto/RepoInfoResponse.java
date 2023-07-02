package com.Teletubbies.Apollo.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RepoInfoResponse {
    @JsonProperty("name")
    private String repoName;
    @JsonProperty("html_url")
    private String repoURL;
}
