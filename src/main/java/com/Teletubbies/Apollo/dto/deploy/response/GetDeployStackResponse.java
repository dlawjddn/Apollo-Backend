package com.Teletubbies.Apollo.dto.deploy.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GetDeployStackResponse {
    @JsonProperty("serviceId")
    private Long Id;
    @JsonProperty("stackName")
    private String stackName;
    @JsonProperty("stackType")
    private String stackType;
    @JsonProperty("endpoint")
    private String endPoint;
    @JsonProperty("content")
    private String content;

    public GetDeployStackResponse(Long Id, String stackName, String stackType, String endPoint, String content) {
        this.Id = Id;
        this.stackName = stackName;
        this.stackType = stackType;
        this.endPoint = endPoint;
        this.content = content;
    }
}
