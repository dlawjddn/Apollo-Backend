package com.Teletubbies.Apollo.credential.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class CredentialDto {
    @JsonProperty("AWSAccountId")
    private String awsAccountId;
    @JsonProperty("AWSAccessKey")
    private String accessKey;
    @JsonProperty("AWSSecretKey")
    private String secretKey;
    @JsonProperty("AWSRegion")
    private String region;
    @JsonProperty("GithubOAuthToken")
    private String githubOAuthToken;
}
