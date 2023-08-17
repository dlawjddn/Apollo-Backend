package com.Teletubbies.Apollo.board.service;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.dto.request.SavePostRequest;
import com.Teletubbies.Apollo.board.dto.request.UpdatePostRequest;
import com.Teletubbies.Apollo.board.dto.response.FindPostResponse;
import com.Teletubbies.Apollo.board.dto.response.UpdatePostResponse;
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
}
