package com.Teletubbies.Apollo.credential.service;

import com.Teletubbies.Apollo.credential.domain.Credential;
import com.Teletubbies.Apollo.credential.repository.CredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CredentialService {
    private final CredentialRepository credentialRepository;

    @Autowired
    public CredentialService(CredentialRepository credentialRepository) {
        this.credentialRepository = credentialRepository;
    }

    public Credential saveCredential(Credential credential) {
        return credentialRepository.save(credential);
    }

    public void deleteCredential(Credential credential) {
        credentialRepository.delete(credential);
    }
}
