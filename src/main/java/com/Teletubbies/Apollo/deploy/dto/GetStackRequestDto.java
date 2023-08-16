package com.Teletubbies.Apollo.deploy.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetStackRequestDto {
    @JsonProperty("repoName")
    private String repoName;
    @JsonProperty("stackType")
    private String stackType;
}
