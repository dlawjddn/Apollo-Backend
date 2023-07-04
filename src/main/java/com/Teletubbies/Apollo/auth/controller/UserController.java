package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.domain.User;
import com.Teletubbies.Apollo.auth.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/login/find/id")
    public String findUserById(){
        Long id = 103355883L;
        User findUser = userService.getUserById(id);
        return findUser.getName();
    }
    @GetMapping("/login/find/login")
    public String findUserByLogin(){
        String login = "dlawjddn";
        User findUser = userService.getUserByLogin(login);
        return findUser.getName();
    }
    @GetMapping("/login/find/name")
    public String findUserByName(){
        String name = "임정우";
        User findUser = userService.getUserByName(name);
        return findUser.getName();
    }
}
