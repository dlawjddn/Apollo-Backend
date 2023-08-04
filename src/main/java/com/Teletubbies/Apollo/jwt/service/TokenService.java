package com.Teletubbies.Apollo.jwt.service;

import com.Teletubbies.Apollo.jwt.domain.Token;
import com.Teletubbies.Apollo.jwt.repository.TokenRepository;
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
