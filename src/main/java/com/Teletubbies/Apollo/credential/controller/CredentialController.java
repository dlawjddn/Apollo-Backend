package com.Teletubbies.Apollo.credential.controller;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.credential.domain.Credential;
import com.Teletubbies.Apollo.credential.service.CredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CredentialController {
    private final CredentialService credentialService;
    private final UserService userService;

    @PostMapping("/credential/{userId}")
    public ResponseEntity<Credential> createCredential(@PathVariable Long userId, @RequestBody Credential credential) {
        ApolloUser user = userService.getUserById(userId);
        try {
            credential.setApolloUser(user);
            return ResponseEntity.ok(credentialService.saveCredential(credential));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/credential/{userId}")
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
