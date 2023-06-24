package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.dto.AccessTokenRequest;
import com.Teletubbies.Apollo.auth.dto.AccessTokenResponse;
import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import com.Teletubbies.Apollo.auth.oauth.service.OauthService;
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
    private final OauthService oauthService;
    public AccessTokenController(OauthService oauthService){
        this.oauthService = oauthService;
    }
    @GetMapping("/repository")
    public ResponseEntity<String> getUserInfo(@RequestParam String code) {
        String accessToken = oauthService.getAccessToken(code);
        String userName = oauthService.getUserName(accessToken);
        return ResponseEntity.ok(userName);
    }
}
