package com.Teletubbies.Apollo.jwt.repository;

import com.Teletubbies.Apollo.jwt.domain.ApolloUserToken;
import com.Teletubbies.Apollo.jwt.domain.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
    List<UserAuthority> findAllByApolloUserToken(ApolloUserToken apolloUserToken);
}
