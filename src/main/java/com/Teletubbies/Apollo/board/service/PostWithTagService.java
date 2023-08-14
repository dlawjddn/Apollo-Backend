package com.Teletubbies.Apollo.board.service;

import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.domain.PostWithTag;
import com.Teletubbies.Apollo.board.domain.Tag;
import com.Teletubbies.Apollo.board.repository.PostWithTagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostWithTagService {
    private final PostWithTagRepository postWithTagRepository;
    @Transactional
    public Long savePostWithTag(Post post, Tag tag){
        return postWithTagRepository.save(new PostWithTag(post, tag)).getId();
    }
    public List<PostWithTag> findPostWithTagByPost(Post post){
        return postWithTagRepository.findAllByPost(post);
    }
    @Transactional
    public void deletePostWithTag(PostWithTag postWithTag){
        postWithTagRepository.delete(postWithTag);
    }

}
