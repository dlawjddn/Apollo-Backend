package com.Teletubbies.Apollo.board.controller;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.board.domain.Comment;
import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.domain.PostWithTag;
import com.Teletubbies.Apollo.board.dto.comment.response.CommentInPostResponse;
import com.Teletubbies.Apollo.board.dto.post.request.DeletePostRequest;
import com.Teletubbies.Apollo.board.dto.post.request.SavePostRequest;
import com.Teletubbies.Apollo.board.dto.post.request.UpdatePostRequest;
import com.Teletubbies.Apollo.board.dto.post.response.*;
import com.Teletubbies.Apollo.board.dto.tag.ConvertTag;
import com.Teletubbies.Apollo.board.service.CommentService;
import com.Teletubbies.Apollo.board.service.PostService;
import com.Teletubbies.Apollo.board.domain.Tag;
import com.Teletubbies.Apollo.board.service.PostWithTagService;
import com.Teletubbies.Apollo.board.service.TagService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    private final CommentService commentService;
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
    @GetMapping("/board")
    public StartBoard findAllPosts(@RequestParam int pageNum){
        PageRequest pageRequest = PageRequest.of(pageNum - 1, 3, Sort.by("createAt").descending());
        return new StartBoard(postService.findAllPosts(pageRequest), tagService.findAllTag());
    }
    @GetMapping("/tag")
    public List<ConvertTag> findAllTags() {return tagService.findAllTag();}
    @GetMapping("/board/{postId}")
    public PostWithAllDetailResponse findPostDetails(@PathVariable Long postId){
        Post findPost = postService.findPostById(postId);
        log.info("게시글 조회 완료");

        List<ConvertTag> tagOfPost = postWithTagService.findPostWithTagByPost(findPost).stream()
                .map(postWithTag -> new ConvertTag(postWithTag.getTag().getId(), postWithTag.getTag().getName()))
                .toList();
        log.info("게시글의 태그 조회 완료, 태그 dto 변환 완료");

        PostOnlyPostResponse postResponse = new PostOnlyPostResponse(findPost.getApolloUser().getId(), findPost.getId(), findPost.getTitle(), findPost.getContent(), tagOfPost, findPost.getCreateAt());
        log.info("게시글 dto 변환 완료");

        List<Comment> findComments = commentService.findAllCommentByPost(findPost);
        log.info("게시글의 댓글 조회 완료");
        List<CommentInPostResponse> commentResponses = findComments.stream()
                .map(findComment -> new CommentInPostResponse(findComment.getId(), findComment.getApolloUser().getId(), findComment.getContent(), findComment.getCreateAt()))
                .toList();
        log.info("게시글의 댓글 dto 변환 완료");
        return new PostWithAllDetailResponse(postResponse, commentResponses);
    }
    @GetMapping("/board/title/{titleName}")
    public List<PostNoContentResponse> findSimilarPostByTitle(@PathVariable String titleName){
        return postService.findSimilarPostByTitle(titleName);
    }
    @GetMapping("board/titleOrContent/{searchString}")
    public List<PostNoContentResponse> findSimilarPostByTitleOrContent(@PathVariable String searchString){
        return postService.findSimilarPostByTitleOrContent(searchString);
    }
    @PatchMapping("/board/{postId}")
    public UpdatePostResponse updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request){
        Post findPost = postService.findPostById(postId);
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
        log.info("저장되어야 하는 태그 목록 조회 완료");
        List<Tag> deleteTagInUpdate = postWithTagService.findDeleteTagInUpdate(originTagNames, request.getTagNames());
        log.info("삭제 되어야 하는 태그 목록 조회 완료");
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
    @DeleteMapping("/board/{postId}")
    public String deletePost(@PathVariable Long postId, @RequestBody DeletePostRequest request){
        return postService.deletePost(postId, request.getUserId());
    }
}
