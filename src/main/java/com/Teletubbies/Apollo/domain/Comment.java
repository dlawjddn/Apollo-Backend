package com.Teletubbies.Apollo.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApolloUser apolloUser;
    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;
    private String content;
    private Date createAt;
    private Date updateAt;

    public Comment(ApolloUser apolloUser, Post post, String content) {
        this.apolloUser = apolloUser;
        this.post = post;
        this.content = content;
        this.createAt = new Date();
        this.updateAt = new Date();
    }
    public Comment updateComment(String content){
        this.content = content;
        this.updateAt = new Date();
        return this;
    }
}
