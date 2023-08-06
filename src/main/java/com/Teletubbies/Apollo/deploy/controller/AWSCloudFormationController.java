package com.Teletubbies.Apollo.deploy.controller;

import com.Teletubbies.Apollo.deploy.dto.StackRequestDto;
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
    private final AWSCloudFormationService awsCloudFormationService;

    @PostMapping("/cloudformation")
    public ResponseEntity<String> createStack(@RequestBody String repoName,
                                              @RequestBody String stackName,
                                              @RequestBody String type) {
        awsCloudFormationService.createStack(repoName, stackName, type);
        return ResponseEntity.ok(stackName + " is created successfully");
    }

    @Operation(summary = "Client stack 생성", description = "CloudFormation을 이용하여 Client stack을 생성한다.")
    @PostMapping("/cloudformation/client")
    public ResponseEntity<String> createClientStack(
            @RequestBody StackRequestDto stackRequestDto
            ) {
        String repoName = stackRequestDto.getRepoName();
        awsCloudFormationService.createClientStack(repoName);
        return ResponseEntity.ok("Client stack is created successfully");
    }

    @Operation(summary = "Server stack 생성", description = "CloudFormation을 이용하여 Server stack을 생성한다.")
    @PostMapping("/cloudformation/server")
    public ResponseEntity<String> createServerStack(
            @RequestBody StackRequestDto stackRequestDto
            ) {
        String repoName = stackRequestDto.getRepoName();
        awsCloudFormationService.createServerStack(repoName);
        return ResponseEntity.ok("Server stack is created successfully");
    }

    @Operation(summary = "Stack 삭제", description = "요청한 이름의 Stack을 삭제한다.")
    @DeleteMapping("/cloudformation")
    public ResponseEntity<String> deleteStack(@RequestBody StackRequestDto stackRequestDto) {
        String stackName = stackRequestDto.getRepoName();
        awsCloudFormationService.deleteStack(stackName);
        return ResponseEntity.ok(stackName + " is deleted successfully");
    }

    @Operation(summary = "Stack 조회", description = "현재 생성되어 있는 stack을 조회한다.")
    @GetMapping("/cloudformation")
    public ResponseEntity<String> describeStacks() {
        awsCloudFormationService.describeStacks();
        return ResponseEntity.ok("Describe stacks successfully");
    }
}
