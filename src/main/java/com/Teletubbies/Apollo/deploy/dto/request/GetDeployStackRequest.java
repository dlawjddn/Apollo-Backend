package com.Teletubbies.Apollo.deploy.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetDeployStackRequest {
    @JsonProperty("stackName")
    private String stackName;
}
