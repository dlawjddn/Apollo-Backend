package com.Teletubbies.Apollo.credential.domain;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "credential")
public class Credential {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aws_account_id")
    private String awsAccountId;

    @Column(name = "access_key")
    private String accessKey;

    @Column(name = "secret_key")
    private String secretKey;

    private String region;

    @Column(name = "github_oauth_token")
    private String githubOAuthToken;

    @OneToOne
    @JoinColumn(name = "user_id")
    private ApolloUser apolloUser;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;
}
