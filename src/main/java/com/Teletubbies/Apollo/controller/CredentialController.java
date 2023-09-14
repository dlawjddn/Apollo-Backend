package com.Teletubbies.Apollo.controller;

import com.Teletubbies.Apollo.service.UserService;
import com.Teletubbies.Apollo.dto.credential.response.DeleteCredentialResponse;
import com.Teletubbies.Apollo.dto.credential.response.GetCredentialResponse;
import com.Teletubbies.Apollo.dto.credential.response.PatchCredentialResponse;
import com.Teletubbies.Apollo.dto.credential.response.PostCredentialResponse;
import com.Teletubbies.Apollo.dto.credential.request.PatchCredentialRequest;
import com.Teletubbies.Apollo.dto.credential.request.PostCredentialRequest;
import com.Teletubbies.Apollo.service.CredentialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Credential Controller", description = "Credential Controller 관련 Rest API")
public class CredentialController {
    private final CredentialService credentialService;
    private final UserService userService;
    @Operation(summary = "Credential 생성", description = "Credential을 생성한다.")
    @PostMapping("/credential/{userId}")
    public PostCredentialResponse createCredential(@PathVariable Long userId,
                                                  @RequestBody PostCredentialRequest request) {
        PostCredentialResponse response = new PostCredentialResponse();
        credentialService.saveCredential(userId, request);
        response.setContent("Credential is created successfully");
        return response;
    }

    @Operation(summary = "Credential 수정", description = "Credential을 수정한다.")
    @PatchMapping("/credential/{userId}")
    public PatchCredentialResponse updateCredential(@PathVariable Long userId,
                                                    @RequestBody PatchCredentialRequest request) {
        PatchCredentialResponse response = new PatchCredentialResponse();
        credentialService.updateCredential(userId, request);
        response.setContent("Credential is updated successfully");
        return response;
    }

    @Operation(summary = "Credential 조회", description = "Credential을 조회한다.")
    @GetMapping("/credential/{userId}")
    public GetCredentialResponse getCredential(@PathVariable Long userId) {
        return credentialService.readCredential(userId);
    }

    @Operation(summary = "Credential 삭제", description = "Credential을 삭제한다.")
    @DeleteMapping("/credential/{userId}")
    public DeleteCredentialResponse deleteCredential(@PathVariable Long userId) {
        credentialService.deleteCredential(userId);
        DeleteCredentialResponse response = new DeleteCredentialResponse();
        response.setContent("Credential is deleted successfully");
        return response;
    }
}
