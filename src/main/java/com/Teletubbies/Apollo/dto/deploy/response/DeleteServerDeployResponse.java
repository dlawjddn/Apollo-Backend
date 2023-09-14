package com.Teletubbies.Apollo.dto.deploy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeleteServerDeployResponse {
    @JsonProperty("content")
    private String content;
}
