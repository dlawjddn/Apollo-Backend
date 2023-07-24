package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import com.Teletubbies.Apollo.auth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AccessTokenController {
    private final OAuthService oAuthService;
    @PostMapping("/authenticate")
    public ResponseEntity<?> getUsersInfo(@RequestParam String code) {
        try {
            String accessToken = oAuthService.getAccessToken(code);
            if (accessToken == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("access-token 발행 실패");
            }
            log.info("access-token 발행 성공: " + accessToken);
            MemberInfoResponse memberInfoResponse = oAuthService.getUserInfo(accessToken).getBody();
            if (memberInfoResponse == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유저 정보 객체 dto 변환 실패");
            }
            log.info("유저 정보 객체 dto 변환 성공: " + memberInfoResponse);
            return ResponseEntity.status(HttpStatus.OK).body(memberInfoResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("유저 정보 객체 dto 변환 실패: " + e.getMessage());
        }
    }
}
