package com.Teletubbies.Apollo.service;

import com.Teletubbies.Apollo.domain.Token;
import com.Teletubbies.Apollo.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TokenService {
    private final TokenRepository tokenRepository;
    @Transactional
    public Token saveToken(Token token){
        Token savedToken = tokenRepository.save(token);
        return savedToken;
    }

}
