package com.Teletubbies.Apollo.repository;

import com.Teletubbies.Apollo.domain.Post;
import com.Teletubbies.Apollo.domain.PostWithTag;
import com.Teletubbies.Apollo.domain.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostWithTagRepository extends JpaRepository<PostWithTag, Long> {
    List<PostWithTag> findAllByTag(Tag tag);
    Page<PostWithTag> findPostWithTagByTag(Tag tage, Pageable pageable);
    List<PostWithTag> findAllByPost(Post post);
    Optional<PostWithTag> findByPostAndTag(Post post, Tag tag);
    Long countByTag(Tag tag);
}
