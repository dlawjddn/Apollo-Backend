package com.Teletubbies.Apollo.auth.repository;

import com.Teletubbies.Apollo.auth.domain.Repo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RepoRepository extends JpaRepository<Repo, Long> {
    List<Repo> findByOwnerLogin (String login);
    Optional<Repo> findByRepoName (String name);
    Optional<Repo> findByRepoUrl (String repoURL);

}
