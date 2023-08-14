package com.Teletubbies.Apollo.board.controller;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.domain.PostWithTag;
import com.Teletubbies.Apollo.board.dto.request.SavePostRequest;
import com.Teletubbies.Apollo.board.dto.request.UpdatePostRequest;
import com.Teletubbies.Apollo.board.dto.response.SavePostResponse;
import com.Teletubbies.Apollo.board.dto.response.UpdatePostResponse;
import com.Teletubbies.Apollo.board.service.PostService;
import com.Teletubbies.Apollo.board.domain.Tag;
import com.Teletubbies.Apollo.board.service.PostWithTagService;
import com.Teletubbies.Apollo.board.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public SavePostResponse registerPost(@RequestBody SavePostRequest request){
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

        return new SavePostResponse(post.getId(), findUser.getId());
    }
    @PatchMapping("/board")
    public UpdatePostResponse updatePost(@RequestBody UpdatePostRequest request){
        Post findPost = postService.findPostById(request.getPostId());
        log.info("게시글 조회 성공");

        Post updatedPost= postService.updatePost(findPost, request.getTitle(), request.getContent());
        log.info("게시글 업데이트 성공");

        List<PostWithTag> findPostWithTags = postWithTagService.findPostWithTagByPost(updatedPost);
        log.info("태그와 게시물 연관 객체 조회 성공");

        List<String> keepTagNames = new ArrayList<>(); // 게시글 & 태그 관계 유지 -> 기존과 새로운 리스트 중복 관계
        List<String> saveTagNames = new ArrayList<>(); // 게시글 & 태그 관계 새로 생성 -> 기존에는 없는 새로운 리스트에는 있는 관계
        List<String> deleteTagNames = new ArrayList<>(); // 게시글 & 태그 관계 삭제 -> 기존에는 있는데 새로운 리스트에는 없는 관계

        List<String> originTagNames = new ArrayList<>(); //  기존 태그 리스트
        for (PostWithTag findPostWithTag : findPostWithTags) {
            originTagNames.add(findPostWithTag.getTag().getName());
        }

        List<String> newTagNames = request.getTagNames(); // 새로운 태그 리스트

        for (String originTagName : originTagNames) { // keepTagNames, deleteTagNames 생성완료
            int count = 0;
            for (String newTagName : newTagNames) {
                if (originTagName.equals(newTagName)){
                    keepTagNames.add(originTagName);
                    log.info("keep: "+originTagName);
                }
                else count++;
            }
            if (count == newTagNames.size()) {
                deleteTagNames.add(originTagName);
                log.info("delete: " + originTagName);
            }
        }

        for (String newTagName : newTagNames) {
            int count = 0;
            for (String originTagName : originTagNames) {
                if (originTagName.equals(newTagName)) break; //  keep && new
                count++;
            }
            if (count == originTagNames.size()) {
                saveTagNames.add(newTagName);
                log.info("new: " + newTagName);
            }
        }

        return new UpdatePostResponse(updatedPost.getId());
    }
}
