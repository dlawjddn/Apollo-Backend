package com.Teletubbies.Apollo.credential.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class PatchCredentialResponse {
    @JsonProperty("content")
    private String content;
}
