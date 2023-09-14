package com.Teletubbies.Apollo.dto.deploy.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostServerDeployRequest {
    @JsonProperty("repoName")
    private String repoName;
}
