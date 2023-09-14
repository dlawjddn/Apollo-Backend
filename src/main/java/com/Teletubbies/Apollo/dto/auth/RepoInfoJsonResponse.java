package com.Teletubbies.Apollo.dto.auth;

import com.Teletubbies.Apollo.domain.ApolloUser;
import lombok.Data;

@Data
public class RepoInfoJsonResponse {
    private String userLogin;
    private String repoName;
    private String repoUrl;
    private ApolloUser apolloUser;
}
