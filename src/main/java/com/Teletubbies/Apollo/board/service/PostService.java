package com.Teletubbies.Apollo.board.service;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.dto.post.request.DeletePostRequest;
import com.Teletubbies.Apollo.board.dto.post.request.SavePostRequest;
import com.Teletubbies.Apollo.board.dto.post.response.FindPostResponse;
import com.Teletubbies.Apollo.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostWithTagService postWithTagService;
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
                        findPost.getContent()))
                .toList();

    }
    public List<FindPostResponse> findSimilarPostByTitle(String title){
        List<Post> findPosts = postRepository.findByTitleContainingIgnoreCase(title);
        return findPosts.stream()
                .map(findPost -> new FindPostResponse(
                        findPost.getId(),
                        findPost.getApolloUser().getId(),
                        findPost.getTitle(),
                        findPost.getContent()))
                .collect(Collectors.toList());

    }
    public List<FindPostResponse> findSimilarPostByTitleOrContent(String searchString){
        List<Post> findPosts = postRepository.findByContentContainingIgnoreCaseOrTitleContainingIgnoreCase(searchString, searchString);
        return findPosts.stream()
                .map(findPost -> new FindPostResponse(
                        findPost.getId(),
                        findPost.getApolloUser().getId(),
                        findPost.getTitle(),
                        findPost.getContent()))
                .collect(Collectors.toList());
    }
    //update
    @Transactional
    public Post updatePost(Post post, String title, String content){
        return post.updatePost(title, content);
    }
    @Transactional
    public String deletePost(DeletePostRequest request){
        Post findPost = findPostById(request.getPostId());
        if (!findPost.getApolloUser().getId().equals(request.getUserId()))
            throw new IllegalArgumentException("작성자와 수정자는 동일한 사람이여야 합니다.");
        postWithTagService.findPostWithTagByPost(findPost).stream()
                        .forEach(postWithTag -> postWithTagService.deletePostWithTag(postWithTag));
        log.info("연관관계 삭제 완료");
        //postRepository.delete(findPost);
        log.info("게시글 삭제 완료");
        return "ok";
    }
}
