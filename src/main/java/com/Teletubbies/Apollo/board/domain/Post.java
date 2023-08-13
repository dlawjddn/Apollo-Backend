package com.Teletubbies.Apollo.board.domain;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApolloUser apolloUser;
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    private Date createAt;
    private Date updateAt;

    public Post(ApolloUser apolloUser, String title, String content, Date createAt) {
        this.apolloUser = apolloUser;
        this.title = title;
        this.content = content;
        this.createAt = createAt;
        this.updateAt = createAt;
    }
}
