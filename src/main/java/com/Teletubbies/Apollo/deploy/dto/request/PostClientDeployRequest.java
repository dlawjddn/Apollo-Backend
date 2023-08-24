package com.Teletubbies.Apollo.deploy.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostClientDeployRequest {
    @JsonProperty("repoName")
    private String repoName;
}
