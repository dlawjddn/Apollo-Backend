package com.Teletubbies.Apollo.credential.dto.reponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
public class PostCredentialResponse {
    @JsonProperty("content")
    private String content;
}
