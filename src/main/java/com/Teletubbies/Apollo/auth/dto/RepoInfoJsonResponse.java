package com.Teletubbies.Apollo.auth.dto;

import lombok.Data;

@Data
public class RepoInfoJsonResponse {
    private String userLogin;
    private String repoName;
    private String repoUrl;
}
