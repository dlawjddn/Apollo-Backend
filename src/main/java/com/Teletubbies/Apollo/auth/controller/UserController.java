package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.domain.User;
import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.jwt.dto.TokenInfo;
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
        log.info("json 받기 성공");
        log.info("user login: " + userLoginRequestDto.getUserLogin());
        log.info("user id: " + userLoginRequestDto.getUserId());
        return "json ok";
    }
    @GetMapping("/login/find/{userId}")
    public String findUserById(@PathVariable Long userId){
        // 103355883 -> dlawjddn id
        User findUser = userService.getUserById(userId);
        return findUser.getName();
    }
    @GetMapping("/login/find/login/{userLogin}")
    public String findUserByLogin(@PathVariable String userLogin){
        User findUser = userService.getUserByLogin(userLogin);
        return findUser.getName();
    }
    @GetMapping("/login/find/name/{userName}")
    public String findUserByName(@PathVariable String userName){
        User findUser = userService.getUserByName(userName);
        return findUser.getName();
    }
}
