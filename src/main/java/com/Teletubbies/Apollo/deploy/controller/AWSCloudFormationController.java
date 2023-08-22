package com.Teletubbies.Apollo.deploy.controller;

import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.deploy.dto.request.DeleteClientDeployRequest;
import com.Teletubbies.Apollo.deploy.dto.request.PostClientDeployRequest;
import com.Teletubbies.Apollo.deploy.dto.request.PostServerDeployRequest;
import com.Teletubbies.Apollo.deploy.dto.response.*;
import com.Teletubbies.Apollo.deploy.service.AWSCloudFormationClientService;
import com.Teletubbies.Apollo.deploy.service.AWSCloudFormationServerService;
import com.Teletubbies.Apollo.deploy.service.AWSCloudFormationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "AWS CloudFormation Controller", description = "AWS CloudFormation Controller 관련 Rest API")
public class AWSCloudFormationController {
    private final AWSCloudFormationClientService awsCloudformationClientService;
    private final AWSCloudFormationServerService awsCloudFormationServerService;
    private final UserService userService;
    private final AWSCloudFormationService awsCloudFormationService;

    @Operation(summary = "Client stack 생성", description = "CloudFormation을 이용하여 Client stack을 생성한다.")
    @PostMapping("/cloudformation/{userId}/client")
    public PostClientDeployResponse createClientStack(
            @PathVariable Long userId,
            @RequestBody PostClientDeployRequest request
    ) {
        return awsCloudformationClientService.saveService(userId, request);
    }

    @Operation(summary = "Client stack 제거", description = "CloudFormation을 이용하여 Client stack을 제거한다.")
    @DeleteMapping("/cloudformation/{userId}/client")
    public DeleteClientDeployResponse deleteClientStack(
            @PathVariable Long userId,
            @RequestBody DeleteClientDeployRequest request
    ) {
        String repoName = request.getRepoName();
        awsCloudformationClientService.deleteClientStack(userId, repoName);
        DeleteClientDeployResponse response = new DeleteClientDeployResponse();
        response.setContent("Client stack is deleted successfully");
        return response;
    }


    @Operation(summary = "Server stack 생성", description = "CloudFormation을 이용하여 Server stack을 생성한다.")
    @PostMapping("/cloudformation/{userId}/server")
    public PostServerDeployResponse createServerStack(
            @PathVariable Long userId,
            @RequestBody PostServerDeployRequest request
    ) {
        return awsCloudFormationServerService.saveService(userId, request);
    }

    @Operation(summary = "Server stack 제거", description = "CloudFormation을 이용하여 Server stack을 제거한다.")
    @DeleteMapping("/cloudformation/{userId}/server")
    public DeleteServerDeployResponse deleteServerStack(
            @PathVariable Long userId,
            @RequestBody DeleteClientDeployRequest request
    ) {
        String repoName = request.getRepoName();
        awsCloudFormationServerService.deleteServerStack(userId, repoName);
        DeleteServerDeployResponse response = new DeleteServerDeployResponse();
        response.setContent("Server stack is deleted successfully");
        return response;
    }

    @Operation(summary = "Stack 조회", description = "사용자의 이름을 기반으로 client를 생성한 stack을 조회한다.")
    @GetMapping("/cloudformation/{userId}")
    public List<GetDeployStackResponse> getClientStack(@PathVariable Long userId) {
      return awsCloudFormationService.getDeployStacks(userId);
    }
}
