package com.Teletubbies.Apollo.deploy.controller;

import com.Teletubbies.Apollo.deploy.dto.StackRequestDto;
import com.Teletubbies.Apollo.deploy.service.AWSCloudFormationClientService;
import com.Teletubbies.Apollo.deploy.service.AWSCloudFormationServerService;
import com.Teletubbies.Apollo.deploy.service.AWSCloudFormationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "AWS CloudFormation Controller", description = "AWS CloudFormation Controller 관련 Rest API")
public class AWSCloudFormationController {
    private final AWSCloudFormationClientService awsCloudformationClientService;
    private final AWSCloudFormationServerService awsCloudFormationServerService;
    private final AWSCloudFormationService awsCloudFormationService;

    @Operation(summary = "Client stack 생성", description = "CloudFormation을 이용하여 Client stack을 생성한다.")
    @PostMapping("/cloudformation/client")
    public ResponseEntity<String> createClientStack(
            @RequestBody StackRequestDto stackRequestDto
            ) {
        String repoName = stackRequestDto.getRepoName();
        String EndPoint = awsCloudformationClientService.createClientStack(repoName);
        return ResponseEntity.ok(EndPoint);
    }

    @Operation(summary = "Client stack 제거", description = "CloudFormation을 이용하여 Client stack을 제거한다.")
    @DeleteMapping("/cloudformation/client")
    public ResponseEntity<String> deleteClientStack(
            @RequestBody StackRequestDto stackRequestDto
            ) {
        String repoName = stackRequestDto.getRepoName();
        awsCloudformationClientService.deleteClientStack(repoName);
        return ResponseEntity.ok("Client stack is deleted successfully");
    }


    @Operation(summary = "Server stack 생성", description = "CloudFormation을 이용하여 Server stack을 생성한다.")
    @PostMapping("/cloudformation/server")
    public ResponseEntity<String> createServerStack(
            @RequestBody StackRequestDto stackRequestDto
            ) {
        String repoName = stackRequestDto.getRepoName();
        awsCloudFormationServerService.createServerStack(repoName);
        return ResponseEntity.ok("Server stack is created successfully");
    }

    @Operation(summary = "Server stack 제거", description = "CloudFormation을 이용하여 Server stack을 제거한다.")
    @DeleteMapping("/cloudformation/server")
    public ResponseEntity<String> deleteServerStack(
            @RequestBody StackRequestDto stackRequestDto
    ) {
        String repoName = stackRequestDto.getRepoName();
        awsCloudFormationServerService.deleteServerStack(repoName);
        return ResponseEntity.ok("Server stack is deleted successfully");
    }

    @Operation(summary = "Stack 삭제", description = "요청한 이름의 Stack을 삭제한다.")
    @DeleteMapping("/cloudformation")
    public ResponseEntity<String> deleteStack(@RequestBody StackRequestDto stackRequestDto) {
        String stackName = stackRequestDto.getRepoName();
        awsCloudFormationService.deleteStack(stackName);
        return ResponseEntity.ok(stackName + " is deleted successfully");
    }
}
