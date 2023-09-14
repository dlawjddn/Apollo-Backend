package com.Teletubbies.Apollo.dto.deploy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeleteClientDeployResponse {
    @JsonProperty("content")
    private String content;
}
