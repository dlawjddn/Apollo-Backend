package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.jwt.dto.UserLoginRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    @PostMapping("/save/user")
    public String saveUser(@RequestBody MemberInfoResponse memberInfoResponse){
        Long savedUserId = userService.saveUser(memberInfoResponse);
        log.info("유저 저장 완료");
        return savedUserId.toString();
    }
    @PostMapping("/login/user")
    public String login(@RequestBody UserLoginRequestDto userLoginRequestDto){
        String userLogin = userLoginRequestDto.getUserLogin();
        String userId = userLoginRequestDto.getUserId();
        log.info("user login: " + userLogin);
        log.info("user id: " + userId);
        String login = userService.login(userLogin, userId);
        return login;
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
