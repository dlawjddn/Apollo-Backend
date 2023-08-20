package com.Teletubbies.Apollo.board.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Table(name = "post_tag_association")
@Getter
public class PostWithTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_tag_association_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public PostWithTag(Post post, Tag tag) {
        this.post = post;
        this.tag = tag;
    }
    public PostWithTag changeTag(Tag tag){
        this.tag = tag;
        return this;
    }
}
