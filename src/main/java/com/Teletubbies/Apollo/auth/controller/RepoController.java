package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.domain.Repo;
import com.Teletubbies.Apollo.auth.domain.User;
import com.Teletubbies.Apollo.auth.repository.RepoRepository;
import com.Teletubbies.Apollo.auth.repository.UserRepository;
import com.Teletubbies.Apollo.auth.service.RepoService;
import com.Teletubbies.Apollo.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RepoController {
    private final UserService userService;
    private final RepoService repoService;

    @GetMapping("/repository/list")
    public String saveRepoList() throws ParseException {
        User findUser = userService.getUserByName("임정우");
        repoService.saveRepo(findUser);
        return "repo list success";
    }
    @GetMapping("/repository/list/find/login")
    public String findByLogin(){
        for (Repo repo : repoService.findByLogin(userService.getUserByLogin("dlawjddn"))) {
            System.out.println("repo name: " + repo.getRepoName());
            System.out.println("repo url:  " + repo.getRepoUrl());
        }
        return "success";
    }
}
