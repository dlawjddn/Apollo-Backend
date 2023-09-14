package com.Teletubbies.Apollo.service;

import com.Teletubbies.Apollo.domain.ApolloUser;
import com.Teletubbies.Apollo.domain.Credential;
import com.Teletubbies.Apollo.dto.credential.response.GetCredentialResponse;
import com.Teletubbies.Apollo.dto.credential.request.PatchCredentialRequest;
import com.Teletubbies.Apollo.dto.credential.request.PostCredentialRequest;
import com.Teletubbies.Apollo.exception.CredentialException;
import com.Teletubbies.Apollo.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.Teletubbies.Apollo.exception.CustomErrorCode.CREDENTIAL_NOT_FOUND_ERROR;

@Service
@RequiredArgsConstructor
public class CredentialService {
    private final CredentialRepository credentialRepository;
    private final UserService userService;

    @Transactional
    public void saveCredential(Long userId, PostCredentialRequest request) {
        ApolloUser user = userService.getUserById(userId);
        credentialRepository.save(new Credential(user, request));
    }

    @Transactional
    public void updateCredential(Long userId, PatchCredentialRequest request) {
        Credential credential = credentialRepository.findByApolloUserId(userId)
                .orElseThrow(() -> new CredentialException(CREDENTIAL_NOT_FOUND_ERROR, "해당 유저의 credential이 없습니다"));
        credential.setAwsAccountId(request.getAWSAccountId());
        credential.setAccessKey(request.getAWSAccessKey());
        credential.setSecretKey(request.getAWSSecretKey());
        credential.setRegion(request.getAWSRegion());
        credential.setGithubOAuthToken(request.getGithubOAuthToken());
        credentialRepository.save(credential);
    }


    public GetCredentialResponse readCredential(Long userId) {
        Credential credential = credentialRepository.findByApolloUserId(userId)
                .orElseThrow(() -> new CredentialException(CREDENTIAL_NOT_FOUND_ERROR, "해당 유저의 credential이 없습니다"));
        GetCredentialResponse response = new GetCredentialResponse();
        response.setGithubOAuthToken(credential.getGithubOAuthToken());
        response.setAWSAccountId(credential.getAwsAccountId());
        response.setAWSAccessKey(credential.getAccessKey());
        response.setAWSSecretKey(credential.getSecretKey());
        response.setAWSRegion(credential.getRegion());
        return response;
    }

    @Transactional
    public void deleteCredential(Long userId) {
        ApolloUser user = userService.getUserById(userId);
        if (user == null) {
            throw new CredentialException(CREDENTIAL_NOT_FOUND_ERROR, "해당 유저의 credential이 없습니다");
        }
        Credential credential = credentialRepository.findByApolloUserId(userId)
                .orElseThrow(() -> new CredentialException(CREDENTIAL_NOT_FOUND_ERROR, "해당 유저의 credential이 없습니다"));
        credentialRepository.delete(credential);
    }
}
