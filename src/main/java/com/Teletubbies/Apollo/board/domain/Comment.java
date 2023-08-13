package com.Teletubbies.Apollo.board.domain;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
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

}
