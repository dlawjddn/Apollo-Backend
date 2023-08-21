package com.Teletubbies.Apollo.deploy.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeleteClientDeployResponse {
    @JsonProperty("content")
    private String content;
}
