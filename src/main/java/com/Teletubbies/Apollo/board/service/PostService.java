package com.Teletubbies.Apollo.board.service;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.dto.request.SavePostRequest;
import com.Teletubbies.Apollo.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    @Transactional
    public Post savePost(ApolloUser apolloUser, SavePostRequest savePostRequest){
        log.info("서비스 단 진입 완료");
        return postRepository.save(new Post(apolloUser, savePostRequest.getTitle(), savePostRequest.getContent()));
    }
}
