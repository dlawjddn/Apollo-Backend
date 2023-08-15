package com.Teletubbies.Apollo.board.repository;

import com.Teletubbies.Apollo.board.domain.Post;
import com.Teletubbies.Apollo.board.domain.PostWithTag;
import com.Teletubbies.Apollo.board.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostWithTagRepository extends JpaRepository<PostWithTag, Long> {
    List<PostWithTag> findAllByTag(Tag tag);
    List<PostWithTag> findAllByPost(Post post);
    Optional<PostWithTag> findByPostAndTag(Post post, Tag tag);
}
