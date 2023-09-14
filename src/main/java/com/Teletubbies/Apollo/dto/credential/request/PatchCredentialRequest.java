package com.Teletubbies.Apollo.dto.credential.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class PatchCredentialRequest {
    @JsonProperty("AWSAccountId")
    private String AWSAccountId;
    @JsonProperty("AWSAccessKey")
    private String AWSAccessKey;
    @JsonProperty("AWSSecretKey")
    private String AWSSecretKey;
    @JsonProperty("AWSRegion")
    private String AWSRegion;
    @JsonProperty("GithubOAuthToken")
    private String GithubOAuthToken;
}
