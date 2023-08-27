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

import javax.naming.directory.SearchResult;
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
    public SavePostResponse savePost(@RequestBody SavePostRequest request){
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
    public StartBoard findDataForBoardPage(@RequestParam int pageNum){
        PageRequest pageRequest = PageRequest.of(pageNum - 1, 20, Sort.by("createAt").descending());
        return new StartBoard(postService.countAllPosts(), postService.findAllPosts(pageRequest), tagService.findAllTag());
    }
    @GetMapping("/tag")
    public List<ConvertTag> findAllTags() {return tagService.findAllTag();}
    @GetMapping("/board/{postId}")
    public PostWithAllDetailResponse findPostWithAllInformation(@PathVariable Long postId){
        Post findPost = postService.findPostById(postId);
        log.info("게시글 조회 완료");

        List<ConvertTag> tagOfPost = postWithTagService.findPostWithTagByPost(findPost).stream()
                .map(postWithTag -> new ConvertTag(postWithTag.getTag().getId(), postWithTag.getTag().getName()))
                .toList();
        log.info("게시글의 태그 조회 완료, 태그 dto 변환 완료");

        PostOnlyPostResponse postResponse = new PostOnlyPostResponse(
                findPost.getApolloUser().getId(),
                findPost.getId(),
                findPost.getTitle(),
                findPost.getContent(),
                tagOfPost,
                findPost.getCreateAt());
        log.info("게시글 dto 변환 완료");

        List<CommentInPostResponse> commentResponses = commentService.findAllCommentByPost(findPost).stream()
                .map(findComment -> new CommentInPostResponse(
                        findComment.getId(),
                        findComment.getApolloUser().getId(),
                        findComment.getContent(),
                        findComment.getCreateAt()))
                .toList();
        log.info("게시글의 댓글 조회 완료, dto 변환 완료");
        return new PostWithAllDetailResponse(postResponse, commentResponses);
    }
    @GetMapping("/board/associate-with")
    public PostSearchResponse findAllPostByTag(@RequestParam Long tagId, @RequestParam int pageNum){
        PageRequest pageRequest = PageRequest.of(pageNum - 1, 20, Sort.by(Sort.Direction.DESC, "post"));
        Tag findTag = tagService.findByTagId(tagId);
        return new PostSearchResponse(postWithTagService.countAssociationByTag(findTag),
                postWithTagService.findPagingPostWithTagByTag(findTag, pageRequest));
    }
    @GetMapping("/board/title/{titleName}/{pageNum}")
    public PostSearchResponse findSimilarPostByTitle(@PathVariable String titleName, @PathVariable int pageNum){
        PageRequest sortPageByNewCreated = PageRequest.of(pageNum - 1, 20, Sort.by(Sort.Direction.DESC, "createAt"));
        return new PostSearchResponse(postService.countPostsHaveSimilarTitle(titleName), postService.findSimilarPostByTitle(titleName, sortPageByNewCreated));
    }
    @GetMapping("board/titleOrContent/{searchString}/{pageNum}")
    public PostSearchResponse findSimilarPostByTitleOrContent(@PathVariable String searchString, @PathVariable int pageNum){
        PageRequest sortPageByNewCreated = PageRequest.of(pageNum - 1, 20, Sort.by(Sort.Direction.DESC, "createAt"));
        return new PostSearchResponse(postService.countPostsHaveSimilarTitleOrSimilarContent(searchString),postService.findSimilarPostByTitleOrContent(searchString, sortPageByNewCreated));
    }
    @PatchMapping("/board/{postId}")
    public UpdatePostResponse updatePost(@PathVariable Long postId, @RequestBody UpdatePostRequest request){
        Post updatedPost= postService.updatePost(postService.findPostById(postId), request.getTitle(), request.getContent());
        log.info("게시글 내용 업데이트 성공");

        List<String> originTagNames = postWithTagService.findPostWithTagByPost(updatedPost).stream()
                .map(findPostWithTag -> findPostWithTag.getTag().getName()).toList();;
        log.info("게시글에 할당된 태그들 조회 및 dto 변환 성공");

        postWithTagService.updateAssociationPostAndTag(updatedPost, originTagNames, request.getTagNames());
        log.info("게시글 tag 연관관계 재정의, 연관관계 전혀 없는 태그 삭제 완료");

        return new UpdatePostResponse(updatedPost.getId());
    }
    @DeleteMapping("/board/{postId}")
    public String deletePost(@PathVariable Long postId, @RequestBody DeletePostRequest request){
        return postService.deletePost(postId, request.getUserId());
    }
}
