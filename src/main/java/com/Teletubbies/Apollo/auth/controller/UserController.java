package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.jwt.domain.ApolloUserToken;
import com.Teletubbies.Apollo.jwt.service.AuthorityService;
import com.Teletubbies.Apollo.jwt.service.JwtService;
import com.Teletubbies.Apollo.jwt.service.UserAuthorityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    private final AuthorityService authorityService;
    private final JwtService jwtService;
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
    @GetMapping("/login/find/{userId}")
    public String findUserById(@PathVariable Long userId){
        // 103355883 -> dlawjddn id
        ApolloUser findApolloUser = userService.getUserById(userId);
        return findApolloUser.getName();
    }
    @GetMapping("/login/find/login/{userLogin}")
    public String findUserByLogin(@PathVariable String userLogin){
        ApolloUser findApolloUser = userService.getUserByLogin(userLogin);
        return findApolloUser.getName();
    }
    @GetMapping("/login/find/name/{userName}")
    public String findUserByName(@PathVariable String userName){
        ApolloUser findApolloUser = userService.getUserByName(userName);
        return findApolloUser.getName();
    }
}
