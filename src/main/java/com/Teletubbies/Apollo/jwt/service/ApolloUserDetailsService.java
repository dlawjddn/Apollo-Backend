package com.Teletubbies.Apollo.jwt.service;

import com.Teletubbies.Apollo.jwt.domain.ApolloUserToken;
import com.Teletubbies.Apollo.jwt.repository.ApolloUserTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApolloUserDetailsService implements UserDetailsService {
    private final ApolloUserTokenRepository apolloUserTokenRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        log.info("ApolloUserDeatailService 진입 성공");
        Optional<ApolloUserToken> findUser = apolloUserTokenRepository.findByUserLogin(userLogin);
        log.info("find-user login: ", findUser.get().getUserLogin());
        return apolloUserTokenRepository.findByUserLogin(userLogin)
                .map(this::createUserDetail)
                .orElseThrow();
    }
    public UserDetails createUserDetail(ApolloUserToken apolloUserToken){
        return User.builder()
                .username(apolloUserToken.getUsername())
                .password(passwordEncoder.encode(apolloUserToken.getPassword()))
                .build();
    }
}
