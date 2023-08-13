package com.Teletubbies.Apollo.board.controller;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.dto.SavePostRequest;
import com.Teletubbies.Apollo.board.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;
    private final UserService userService;
    @PostMapping("/register/board")
    public Long registerPost(@RequestBody SavePostRequest request){
        log.info("컨트롤러 단 진입 완료");
        ApolloUser findUser = userService.getUserById(request.getUserId());
        Post post = postService.savePost(findUser, request);
        log.info("게시글 저장 완료");
        return post.getId();
    }
}
