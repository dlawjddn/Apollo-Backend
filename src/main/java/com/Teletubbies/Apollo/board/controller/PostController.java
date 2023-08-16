package com.Teletubbies.Apollo.board.controller;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.domain.PostWithTag;
import com.Teletubbies.Apollo.board.dto.request.SavePostRequest;
import com.Teletubbies.Apollo.board.dto.request.UpdatePostRequest;
import com.Teletubbies.Apollo.board.dto.response.FindPostResponse;
import com.Teletubbies.Apollo.board.dto.response.SavePostResponse;
import com.Teletubbies.Apollo.board.dto.response.UpdatePostResponse;
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
    public SavePostResponse registerPost(@RequestBody SavePostRequest request){
        log.info("컨트롤러 단 진입 완료");
        ApolloUser findUser = userService.getUserById(request.getUserId());

        Post post = postService.savePost(findUser, request);
        log.info("게시글 저장 완료");

        List<Tag> tags = tagService.saveTags(request.getTagNames());
        log.info("태그 저장 완료");

        tags.forEach(tag -> postWithTagService.savePostWithTag(post, tag));
        log.info("태그와 게시물 연관 저장 완료");

        return new SavePostResponse(post.getId(), findUser.getId());
    }
    @GetMapping("/board/title/{titleName}")
    public List<FindPostResponse> findSimilarPostByTitle(@PathVariable String titleName){
        return postService.findSimilarPostByTitle(titleName);
    }
    @PatchMapping("/board")
    public UpdatePostResponse updatePost(@RequestBody UpdatePostRequest request){
        Post findPost = postService.findPostById(request.getPostId());
        log.info("게시글 조회 성공");

        Post updatedPost= postService.updatePost(findPost, request.getTitle(), request.getContent());
        log.info("게시글 업데이트 성공");

        List<PostWithTag> findPostWithTags = postWithTagService.findPostWithTagByPost(updatedPost);
        log.info("태그와 게시물 연관 객체 조회 성공");

        List<String> originTagNames =  findPostWithTags.stream()
                .map(findPostWithTag -> findPostWithTag.getTag().getName()).toList();
        log.info("기존 게시글에 매핑된 태그 조회 성공");

        // 유지 되어야 하는 태그는 건들 필요가 없음
        List<Tag> saveTagInUpdate = postWithTagService.findSaveTagInUpdate(originTagNames, request.getTagNames());
        List<Tag> deleteTagInUpdate = postWithTagService.findDeleteTagInUpdate(originTagNames, request.getTagNames());
        log.info("게시물의 기존 태그와 수정된 태그 parsing 성공");

        postWithTagService.updatingPostWithOldTag(updatedPost, deleteTagInUpdate);
        log.info("게시글에 더 이상 필요없는 태그 연관관계 삭제 완료");

        deleteTagInUpdate.forEach(deleteTag -> {
                            if (postWithTagService.findPostWithTagByTag(deleteTag).size() == 0)
                                tagService.deleteTag(deleteTag);}
        ); log.info("게시글과 연관 관계가 전혀 없는 태그 삭제 완료");

        postWithTagService.updatingPostWithNewTag(updatedPost, saveTagInUpdate);
        log.info("게시글에 새로 필요한 태그 연관관계 저장");

        return new UpdatePostResponse(updatedPost.getId());
    }
}
