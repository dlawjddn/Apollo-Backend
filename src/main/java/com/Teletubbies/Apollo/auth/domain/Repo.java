package com.Teletubbies.Apollo.auth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class Repo {
    @Id
    @GeneratedValue
    private Long id;
    private String repoName;
    private String repoUrl;
}
