package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import com.Teletubbies.Apollo.auth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccessTokenController {
    private final OAuthService oAuthService;
    @PostMapping("/authenticate")
    public MemberInfoResponse getUserInfo(@RequestParam String code) {
        String accessToken = oAuthService.getAccessToken(code);
        log.info("access-token 발행 성공");
        MemberInfoResponse memberInfoResponse = oAuthService.getUserInfo(accessToken).getBody();
        log.info("유저 정보 객체 dto 변환 성공");

        return memberInfoResponse;
    }
}
