package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.dto.AccessTokenRequest;
import com.Teletubbies.Apollo.auth.dto.AccessTokenResponse;
import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

@RestController
public class AccessTokenController {
    private static final String ACCESS_TOKEN_URL = "https://github.com/login/oauth/access_token";
    private static final String MEMBER_INFO_URL = "https://api.github.com/user";

    private static final RestTemplate restTemplate = new RestTemplate();

    @Value("7600733c0c5ed7849ce6")
    private String clientId;
    @Value("49b1fc4106c9ec0c03875e4fb0279efd7703e0f4")
    private String clientSecret;

    @GetMapping("/repository")
    public ResponseEntity<String> getUserInfo(@RequestParam String code) {
        String accessToken = getAccessToken(code);
        String userName = getUserName(accessToken);
        return ResponseEntity.ok(userName);
    }

    private String getAccessToken(String code) {
        return restTemplate.postForObject(
                ACCESS_TOKEN_URL,
                new AccessTokenRequest(clientId, clientSecret, code),
                AccessTokenResponse.class
        ).getAccessToken();
    }

    public String getUserName(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        return restTemplate.exchange(
                MEMBER_INFO_URL,
                HttpMethod.GET,
                request,
                MemberInfoResponse.class
        ).getBody().getName();
    }
}
