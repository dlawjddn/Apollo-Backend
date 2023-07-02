package com.Teletubbies.Apollo.auth.controller;

import com.Teletubbies.Apollo.auth.domain.User;
import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import com.Teletubbies.Apollo.auth.dto.RepoInfoResponse;
import com.Teletubbies.Apollo.auth.repository.UserRepository;
import com.Teletubbies.Apollo.auth.service.OAuthService;
import com.Teletubbies.Apollo.auth.service.UserService;
import org.hibernate.mapping.Map;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class AccessTokenController {
    private final OAuthService oAuthService;
    private final UserService userService;
    private String access_token;
    private final UserRepository userRepository;

    public AccessTokenController(OAuthService oAuthService, UserService userService,
                                 UserRepository userRepository) {
        this.oAuthService = oAuthService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/repository")
    public ResponseEntity<String> getUserInfo(@RequestParam String code) {
        String accessToken = oAuthService.getAccessToken(code);
        access_token = accessToken;
        MemberInfoResponse memberInfoResponse = oAuthService.getUserInfo(accessToken).getBody();
        userService.saveUser(memberInfoResponse);
        return ResponseEntity.ok("success");
    }
    @GetMapping("/repository/list")
    public String getRepoList() throws ParseException {
        User user = userRepository.findByLogin("dlawjddn").get();
        List<RepoInfoResponse> repoURL = oAuthService.getRepoURL(user);
        return "repo list success";
    }
}
