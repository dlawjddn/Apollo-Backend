package com.Teletubbies.Apollo.dto.auth;

public interface TokenProvider {
    String createToken(String code);
    void validateToken(String token);
}
