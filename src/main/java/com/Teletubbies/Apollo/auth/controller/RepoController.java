package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.domain.Repo;
import com.Teletubbies.Apollo.auth.domain.User;
import com.Teletubbies.Apollo.auth.service.RepoService;
import com.Teletubbies.Apollo.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RepoController {
    private final UserService userService;
    private final RepoService repoService;

    @GetMapping("/repository/list")
    public String saveRepoList() throws ParseException {
        User findUser = userService.getUserByName("임정우");
        repoService.saveRepo(findUser);
        return "repo list success";
    }
    @GetMapping("/repository/find/login")
    public String findByLogin(){
        for (Repo repo : repoService.findByLogin(userService.getUserByLogin("dlawjddn"))) {
            System.out.println("repo name: " + repo.getRepoName());
            System.out.println("repo url:  " + repo.getRepoUrl());
        }
        return "repo find by login success";
    }
    @GetMapping("/repository/find/name")
    public String findByRepoName(){
        Repo findRepo = repoService.findByName("algorithm");
        return findRepo.getRepoUrl();
    }
    @GetMapping("/repository/find/url")
    public String findByRepoUrl(){
        Repo findRepo = repoService.findByRepoUrl("https://github.com/dlawjddn/refactoring-library-app");
        return findRepo.getRepoName();
    }
}
