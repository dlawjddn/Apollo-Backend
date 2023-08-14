package com.Teletubbies.Apollo.board.controller;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.dto.request.SavePostRequest;
import com.Teletubbies.Apollo.board.service.PostService;
import com.Teletubbies.Apollo.board.domain.Tag;
import com.Teletubbies.Apollo.board.service.PostWithTagService;
import com.Teletubbies.Apollo.board.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {
    private final PostService postService;
    private final TagService tagService;
    private final PostWithTagService postWithTagService;
    private final UserService userService;
    @PostMapping("/board")
    public Post registerPost(@RequestBody SavePostRequest request){
        log.info("컨트롤러 단 진입 완료");
        ApolloUser findUser = userService.getUserById(request.getUserId());

        Post post = postService.savePost(findUser, request);
        log.info("게시글 저장 완료");

        List<Tag> tags = tagService.saveTag(request.getTagNames());
        log.info("태그 저장 완료");

        for (Tag tag : tags) {
            postWithTagService.savePostWithTag(post, tag);
        }
        log.info("태그와 게시물 연관 저장 완료");

        return post;
    }
    @PatchMapping("/board")
    public void updatePost(){

    }
}
