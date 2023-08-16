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
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;


@RestController
@RequestMapping("/api")
@Slf4j
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

    @Operation(summary = "Credential 수정", description = "Credential을 수정한다.")
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

    @Operation(summary = "Credential 조회", description = "Credential을 조회한다.")
    @GetMapping("/credential/{userId}")
    public ResponseEntity<CredentialDto> getCredential(@PathVariable Long userId) {
        ApolloUser user = userService.getUserById(userId);
        try {
            Credential credential = user.getCredential();
            CredentialDto credentialDto = new CredentialDto();
            credentialDto.setAwsAccountId(credential.getAwsAccountId());
            credentialDto.setAccessKey(credential.getAccessKey());
            credentialDto.setSecretKey(credential.getSecretKey());
            credentialDto.setRegion(credential.getRegion());
            credentialDto.setGithubOAuthToken(credential.getGithubOAuthToken());
            log.info("credential: " + credential);
            return ResponseEntity.ok(credentialDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Credential 삭제", description = "Credential을 삭제한다.")
    @DeleteMapping("/credential/{userId}")
    public ResponseEntity<String> deleteCredential(@PathVariable Long userId) {
        ApolloUser user = userService.getUserById(userId);
        try {
            Credential credential = user.getCredential();
            credentialService.deleteCredential(credential);
            return ResponseEntity.ok("Credential is deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.notFound().build();
    }
}
