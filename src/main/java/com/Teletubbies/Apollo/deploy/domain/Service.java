package com.Teletubbies.Apollo.deploy.domain;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.domain.Repo;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ApolloUser apolloUser;

    @OneToOne
    @JoinColumn(name = "repository_id")
    private Repo repo;

    @Column(name = "stack_name")
    private String stackName;
    @Column(name = "stack_type")
    private String stackType;

    @Column(name = "endpoint")
    private String endpoint;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
