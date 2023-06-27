package com.Teletubbies.Apollo.auth.dto;

public interface TokenProvider {
    String createToken(String code);
    void validateToken(String token);
}
