package com.Teletubbies.Apollo.auth.repository;

import com.Teletubbies.Apollo.auth.domain.Repo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoRepository extends JpaRepository<Repo, Long> {
}
