package com.Teletubbies.Apollo.deploy.controller;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.deploy.domain.ApolloDeployService;
import com.Teletubbies.Apollo.deploy.dto.GetStackRequestDto;
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
    private final UserService userService;
    private final AWSCloudFormationService awsCloudFormationService;

    @Operation(summary = "Client stack 생성", description = "CloudFormation을 이용하여 Client stack을 생성한다.")
    @PostMapping("/cloudformation/{userId}/client")
    public ResponseEntity<String> createClientStack(
            @PathVariable Long userId,
            @RequestBody StackRequestDto stackRequestDto
    ) {
        String repoName = stackRequestDto.getRepoName();
        String EndPoint = awsCloudformationClientService.createClientStack(repoName);
        ApolloUser apolloUser = userService.getUserById(userId);
        ApolloDeployService apolloDeployService = new ApolloDeployService();
        apolloDeployService.setApolloUser(apolloUser);
        apolloDeployService.setStackName(repoName);
        apolloDeployService.setEndpoint(EndPoint);
        apolloDeployService.setStackType("client");
        awsCloudformationClientService.saveService(apolloDeployService);
        return ResponseEntity.ok(EndPoint);
    }

    @Operation(summary = "Client stack 제거", description = "CloudFormation을 이용하여 Client stack을 제거한다.")
    @DeleteMapping("/cloudformation/{userId}client")
    public ResponseEntity<String> deleteClientStack(
            @PathVariable Long userId,
            @RequestBody StackRequestDto stackRequestDto
    ) {
        String repoName = stackRequestDto.getRepoName();
        awsCloudformationClientService.deleteClientStack(repoName);
        return ResponseEntity.ok("Client stack is deleted successfully");
    }


    @Operation(summary = "Server stack 생성", description = "CloudFormation을 이용하여 Server stack을 생성한다.")
    @PostMapping("/cloudformation/{userId}/server")
    public ResponseEntity<String> createServerStack(
            @PathVariable Long userId,
            @RequestBody StackRequestDto stackRequestDto
    ) {
        String repoName = stackRequestDto.getRepoName();
        awsCloudFormationServerService.createServerStack(repoName);
        return ResponseEntity.ok("Server stack is created successfully");
    }

    @Operation(summary = "Server stack 제거", description = "CloudFormation을 이용하여 Server stack을 제거한다.")
    @DeleteMapping("/cloudformation/{userId}/server")
    public ResponseEntity<String> deleteServerStack(
            @PathVariable Long userId,
            @RequestBody StackRequestDto stackRequestDto
    ) {
        String repoName = stackRequestDto.getRepoName();
        awsCloudFormationServerService.deleteServerStack(repoName);
        return ResponseEntity.ok("Server stack is deleted successfully");
    }

    @Operation(summary = "Stack 조회", description = "사용자의 이름을 기반으로 client를 생성한 stack을 조회한다.")
    @GetMapping("/cloudformation/{userId}")
    public ResponseEntity<String> getClientStack(@PathVariable Long userId,
                                                 @RequestBody GetStackRequestDto getStackRequestDto) {
        String stackName = getStackRequestDto.getRepoName();
        String type = getStackRequestDto.getStackType();

        return ResponseEntity.ok("ok");
    }
}
