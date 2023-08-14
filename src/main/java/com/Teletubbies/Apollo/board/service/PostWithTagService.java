package com.Teletubbies.Apollo.board.service;

import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.domain.PostWithTag;
import com.Teletubbies.Apollo.board.domain.Tag;
import com.Teletubbies.Apollo.board.repository.PostWithTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostWithTagService {
    private final PostWithTagRepository postWithTagRepository;
    public void savePostWithTag(Post post, Tag tag){
        postWithTagRepository.save(new PostWithTag(post, tag));
    }
}
