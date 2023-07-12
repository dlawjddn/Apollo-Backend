package com.Teletubbies.Apollo.jwt.test.controller;

import com.Teletubbies.Apollo.jwt.dto.TokenInfo;
import com.Teletubbies.Apollo.jwt.test.dto.MemberLoginRequestDto;
import com.Teletubbies.Apollo.jwt.test.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    public final MemberService memberService;
    @PostMapping("/members/login")
    public TokenInfo login(@RequestBody MemberLoginRequestDto memberLoginRequestDto){
        String memberId = memberLoginRequestDto.getMemberId();
        String password = memberLoginRequestDto.getPassword();
        log.info("json으로 아이디 비번 받아오기 성공");
        TokenInfo tokenInfo = memberService.login(memberId, password);
        log.info("로그인 성공");
        return tokenInfo;
    }
}
