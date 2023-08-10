package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.jwt.domain.ApolloUserToken;
import com.Teletubbies.Apollo.jwt.service.AuthorityService;
import com.Teletubbies.Apollo.jwt.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "User Controller", description = "User CRUD 관련 API")
public class UserController {
    private final UserService userService;
    private final AuthorityService authorityService;
    private final JwtService jwtService;
    @Operation(summary = "User 저장", description = "User 관련 정보 받아서 저장, jwt 인증 기초 과정도 여기서 수행")
    @PostMapping("/save/user")
    public String saveUser(@RequestBody MemberInfoResponse memberInfoResponse){
        ApolloUser savedUser = userService.saveUser(memberInfoResponse);
        log.info("Controller: 유저 저장 완료");
        ApolloUserToken apolloUserToken = jwtService.toMakeTokenSaveObj(savedUser.getLogin(), savedUser.getId().toString());
        if (!authorityService.existAuthority(1L)){
            log.info("권한 정보 저장 시도");
            authorityService.saveAuthority("ROLE_USER");
            log.info("권한 정보 저장 성공");
        }
        jwtService.saveUserAuthority(apolloUserToken, authorityService.findByName("ROLE_USER"));
        log.info("Token 기초 정보 저장 완료");
        return "ok";
    }
    @Operation(summary = "user id 기반으로 회원 조회", description = "쓸 일 없음, 사용 시 수정 필수")
    @GetMapping("/login/find/{userId}")
    public String findUserById(@PathVariable Long userId){
        // 103355883 -> dlawjddn id
        ApolloUser findApolloUser = userService.getUserById(userId);
        return findApolloUser.getName();
    }
    @Operation(summary = "user login 기반으로 회원 조회", description = "쓸 일 없음, 사용 시 수정 필수")
    @GetMapping("/login/find/login/{userLogin}")
    public String findUserByLogin(@PathVariable String userLogin){
        ApolloUser findApolloUser = userService.getUserByLogin(userLogin);
        return findApolloUser.getName();
    }
    @Operation(summary = "user name 기반으로 회원 조회", description = "쓸 일 없음, 사용 시 수정 필수")
    @GetMapping("/login/find/name/{userName}")
    public String findUserByName(@PathVariable String userName){
        ApolloUser findApolloUser = userService.getUserByName(userName);
        return findApolloUser.getName();
    }
}
