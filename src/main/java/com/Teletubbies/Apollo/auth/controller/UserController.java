package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.domain.User;
import com.Teletubbies.Apollo.auth.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
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
