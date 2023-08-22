package com.Teletubbies.Apollo.board.service;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.domain.PostWithTag;
import com.Teletubbies.Apollo.board.domain.Tag;
import com.Teletubbies.Apollo.board.dto.post.request.DeletePostRequest;
import com.Teletubbies.Apollo.board.dto.post.request.SavePostRequest;
import com.Teletubbies.Apollo.board.dto.post.response.FindPostResponse;
import com.Teletubbies.Apollo.board.dto.tag.ConvertTag;
import com.Teletubbies.Apollo.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostWithTagService postWithTagService;
    private final TagService tagService;
    //create
    @Transactional
    public Post savePost(ApolloUser apolloUser, SavePostRequest savePostRequest){
        log.info("서비스 단 진입 완료");
        return postRepository.save(new Post(apolloUser, savePostRequest.getTitle(), savePostRequest.getContent()));
    }
    //read
    public Post findPostById(Long id){
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 아이디입니다."));
    }
    public List<FindPostResponse> findAllPosts(){
        return postRepository.findAll().stream()
                .map(findPost -> new FindPostResponse(
                        findPost.getApolloUser().getId(),
                        findPost.getId(),
                        findPost.getTitle(),
                        tagService.findAllTag(),
                        findPost.getCreateAt()))
                .toList();

    }
    public List<FindPostResponse> findSimilarPostByTitle(String title){
        List<Post> findPosts = postRepository.findByTitleContainingIgnoreCase(title);
        return findPosts.stream()
                .map(findPost -> new FindPostResponse(
                        findPost.getApolloUser().getId(),
                        findPost.getId(),
                        findPost.getTitle(),
                        postWithTagService.findPostWithTagByPost(findPost).stream()
                                .map(postWithTag -> new ConvertTag(postWithTag.getTag()))
                                .toList(),
                        findPost.getCreateAt()))
                .toList();

    }
    public List<FindPostResponse> findSimilarPostByTitleOrContent(String searchString){
        List<Post> findPosts = postRepository.findByContentContainingIgnoreCaseOrTitleContainingIgnoreCase(searchString, searchString);
        return findPosts.stream()
                .map(findPost -> new FindPostResponse(
                        findPost.getApolloUser().getId(),
                        findPost.getId(),
                        findPost.getTitle(),
                        postWithTagService.findPostWithTagByPost(findPost).stream()
                                .map(postWithTag -> new ConvertTag(postWithTag.getTag()))
                                .toList(),
                        findPost.getCreateAt()))
                .toList();
    }
    //update
    @Transactional
    public Post updatePost(Post post, String title, String content){
        return post.updatePost(title, content);
    }

    //delete
    @Transactional
    public String deletePost(DeletePostRequest request){
        Post findPost = findPostById(request.getPostId());
        if (!findPost.getApolloUser().getId().equals(request.getUserId()))
            throw new IllegalArgumentException("작성자와 수정자는 동일한 사람이여야 합니다.");

        List<Tag> tags = new ArrayList<>();
        postWithTagService.findPostWithTagByPost(findPost).stream()
                        .forEach(postWithTag -> {
                            tags.add(postWithTag.getTag()); // 해당 태그에 연관된 게시글이 있는지 파악하기 위해 태그 리스트 추가
                            postWithTagService.deletePostWithTag(postWithTag); //  게시글 & 태그 연관 객체 삭제 -> 게시글도 삭제
                        });
        log.info("삭제된 게시글에 연관된 태그 개수: " + tags.size());
        log.info("연관관계 삭제 완료, cascade 조건으로 게시글도 삭제 완료");

        tags.stream()
                .forEach(tag -> {
                    if (postWithTagService.findPostWithTagByTag(tag).size() == 0) // 어떤 태그와 연관된 게시글이 없는 경우 -> 태그도 삭제
                        tagService.deleteTag(tag);
                });
        log.info("태그에 해당된 게시글 확인 후 삭제 완료");
        return "ok";
    }
}
