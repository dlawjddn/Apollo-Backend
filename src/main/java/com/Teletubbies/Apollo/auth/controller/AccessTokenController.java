package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.dto.SaveUserRequest;
import com.Teletubbies.Apollo.auth.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Access-Token Controller", description = "Gtihub OAuth 관련 API")
public class AccessTokenController {
    private final OAuthService oAuthService;
    @Operation(summary = "Github server에서 실제 유저 정보 받아오기", description = "client에서 넘겨준 code 이용해서 access-token 받아오기, access-token 이용해서 실제 유저 정보 가져오기")
    @PostMapping("/authenticate")
    public ResponseEntity<?> getUsersInfo(@RequestParam String code) {
        try {
            String accessToken = oAuthService.getAccessToken(code);
            if (accessToken == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("access-token 발행 실패");
            }
            log.info("access-token 발행 성공: " + accessToken);
            SaveUserRequest saveUserRequest = oAuthService.getUserInfo(accessToken).getBody();
            if (saveUserRequest == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유저 정보 객체 dto 변환 실패");
            }
            log.info("유저 정보 객체 dto 변환 성공: " + saveUserRequest);
            return ResponseEntity.status(HttpStatus.OK).body(saveUserRequest);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("유저 정보 객체 dto 변환 실패: " + e.getMessage());
        }
    }
}
