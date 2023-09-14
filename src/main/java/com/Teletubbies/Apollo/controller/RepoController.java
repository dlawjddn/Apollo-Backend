package com.Teletubbies.Apollo.controller;

import com.Teletubbies.Apollo.domain.ApolloUser;
import com.Teletubbies.Apollo.domain.Repo;
import com.Teletubbies.Apollo.dto.auth.RepoInfoJsonResponse;
import com.Teletubbies.Apollo.service.RepoService;
import com.Teletubbies.Apollo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Repo Controller", description = "사용자의 repository 관련 api")
public class RepoController {
    private final UserService userService;
    private final RepoService repoService;
    @Operation(summary = "사용자의 repository 목록 저장", description = "사용자의 로그인 값을 이용해서 레포지토리 받아온 뒤, 저장")
    @GetMapping("/repository/list/{userId}")
    public List<RepoInfoJsonResponse> saveRepoList(@PathVariable Long userId) throws ParseException {
        ApolloUser findApolloUser = userService.getUserById(userId);
        List<RepoInfoJsonResponse> repoInfoJsonResponses = repoService.saveRepo(findApolloUser);
        return repoInfoJsonResponses;
    }
    @Operation(summary = "사용자의 login 이용해서 repository 찾기", description = "login 이용 repository 찾기 -> 쓰임새 없음, 쓰인다면 반드시 수정 필요")
    @GetMapping("/repository/find/login/{userLogin}")
    public String findByLogin(@PathVariable String userLogin){
        for (Repo repo : repoService.findByLogin(userService.getUserByLogin(userLogin))) {
            log.info("repo name: " + repo.getRepoName());
            log.info("repo url:  " + repo.getRepoUrl());
        }
        return "repo find by login success";
    }
    @Operation(summary = "repository name 이용해서 repository 찾기", description = "repository name 이용 repository 찾기 -> 쓰임새 없음, 쓰인다면 반드시 수정 필요")
    @GetMapping("/repository/find/name/{repoName}")
    public String findByRepoName(@PathVariable String repoName){
        Repo findRepo = repoService.findByName(repoName);
        return "repo find by repo name success, repo name: " + findRepo.getRepoName();
    }
    @Operation(summary = "repository url 이용해서 repository 찾기", description = "repository url 이용 repository 찾기 -> 쓰임새 없음, 쓰인다면 반드시 수정 필요")
    @GetMapping("/repository/find/url/{repoUrl}")
    public String findByRepoUrl(@PathVariable String repoUrl){
        // "https://github.com/dlawjddn/refactoring-library-app"
        Repo findRepo = repoService.findByRepoUrl(repoUrl);
        return "repo find by repo url success, repo url: " + findRepo.getRepoUrl();
    }
}
