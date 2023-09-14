package com.Teletubbies.Apollo.dto.deploy.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DeleteClientDeployRequest {
    @JsonProperty("serviceId")
    private Long serviceId;
}
