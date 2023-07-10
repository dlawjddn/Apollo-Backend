package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.domain.Repo;
import com.Teletubbies.Apollo.auth.domain.User;
import com.Teletubbies.Apollo.auth.service.RepoService;
import com.Teletubbies.Apollo.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RepoController {
    private final UserService userService;
    private final RepoService repoService;

    @GetMapping("/repository/list/{userLogin}")
    public String saveRepoList(@PathVariable String userLogin) throws ParseException {
        User findUser = userService.getUserByLogin(userLogin);
        repoService.saveRepo(findUser);
        return "repo list success";
    }
    @GetMapping("/repository/find/login/{userLogin}")
    public String findByLogin(@PathVariable String userLogin){
        for (Repo repo : repoService.findByLogin(userService.getUserByLogin(userLogin))) {
            System.out.println("repo name: " + repo.getRepoName());
            System.out.println("repo url:  " + repo.getRepoUrl());
        }
        return "repo find by login success";
    }
    @GetMapping("/repository/find/name/{repoName}")
    public String findByRepoName(@PathVariable String repoName){
        Repo findRepo = repoService.findByName(repoName);
        return "repo find by repo name success, repo name: " + findRepo.getRepoName();
    }
    @GetMapping("/repository/find/url/repoUrl")
    public String findByRepoUrl(@PathVariable String repoUrl){
        // "https://github.com/dlawjddn/refactoring-library-app"
        Repo findRepo = repoService.findByRepoUrl(repoUrl);
        return "repo find by repo url success, repo url: " + findRepo.getRepoUrl();
    }
}
