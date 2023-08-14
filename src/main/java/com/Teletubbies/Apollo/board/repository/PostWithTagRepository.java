package com.Teletubbies.Apollo.board.repository;

import com.Teletubbies.Apollo.board.domain.PostWithTag;
import com.Teletubbies.Apollo.board.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostWithTagRepository extends JpaRepository<PostWithTag, Long> {
    List<PostWithTag> findAllByTag(Tag tag);
}
