package com.Teletubbies.Apollo.dto.credential.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class PatchCredentialResponse {
    @JsonProperty("content")
    private String content;
}
