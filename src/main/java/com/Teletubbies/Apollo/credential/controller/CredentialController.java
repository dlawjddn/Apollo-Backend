package com.Teletubbies.Apollo.credential.controller;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.credential.domain.Credential;
import com.Teletubbies.Apollo.credential.dto.CredentialDto;
import com.Teletubbies.Apollo.credential.service.CredentialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Credential Controller", description = "Credential Controller 관련 Rest API")
public class CredentialController {
    private final CredentialService credentialService;
    private final UserService userService;
    @Operation(summary = "Credential 생성", description = "Credential을 생성한다.")
    @PostMapping("/credential/{userId}")
    public ResponseEntity<Credential> createCredential(@PathVariable Long userId, @RequestBody CredentialDto credentialDto) {
        try {
            ApolloUser user = userService.getUserById(userId);
            Credential credential = new Credential();
            credential.setAwsAccountId(credentialDto.getAwsAccountId());
            credential.setAccessKey(credentialDto.getAccessKey());
            credential.setSecretKey(credentialDto.getSecretKey());
            credential.setRegion(credentialDto.getRegion());
            credential.setGithubOAuthToken(credentialDto.getGithubOAuthToken());
            credential.setApolloUser(user);
            return ResponseEntity.ok(credentialService.saveCredential(credential));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Credential 조회", description = "Credential을 조회한다.")
    @PatchMapping("/credential/{userId}")
    public ResponseEntity<Credential> updateCredential(@PathVariable Long userId, @RequestBody Credential credential) {
        ApolloUser user = userService.getUserById(userId);
        try {
            Credential currentCredential = user.getCredential();
            currentCredential.setAccessKey(credential.getAccessKey());
            currentCredential.setSecretKey(credential.getSecretKey());
            currentCredential.setRegion(credential.getRegion());
            return ResponseEntity.ok(credentialService.saveCredential(currentCredential));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }
}
