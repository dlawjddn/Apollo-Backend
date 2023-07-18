package com.Teletubbies.Apollo.jwt.controller;

import com.Teletubbies.Apollo.jwt.dto.TokenInfo;
import com.Teletubbies.Apollo.jwt.dto.UserLoginRequestDto;
import com.Teletubbies.Apollo.jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class JwtController {
    private final JwtService jwtService;
    @PostMapping("/login/user")
    public String login(@RequestBody UserLoginRequestDto userLoginRequestDto){
        String userLogin = userLoginRequestDto.getUserLogin();
        String userId = userLoginRequestDto.getUserId();
        log.info("user login: " + userLogin);
        log.info("user id: " + userId);
        TokenInfo login = jwtService.login(userLogin, userId);
        return "ok";
    }
}
