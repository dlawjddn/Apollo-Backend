package com.Teletubbies.Apollo.auth.dto;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import lombok.Data;

@Data
public class RepoInfoJsonResponse {
    private String userLogin;
    private String repoName;
    private String repoUrl;
    private ApolloUser apolloUser;
}
