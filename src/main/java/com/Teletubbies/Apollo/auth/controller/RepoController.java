package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.domain.User;
import com.Teletubbies.Apollo.auth.repository.UserRepository;
import com.Teletubbies.Apollo.auth.service.RepoService;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RepoController {
    private final UserRepository userRepository;
    private final RepoService repoService;
    @GetMapping("/repository/list")
    public String saveRepoList() throws ParseException {
        User user = userRepository.findByLogin("dlawjddn").get();
        repoService.saveRepo(user);
        return "repo list success";
    }
}
