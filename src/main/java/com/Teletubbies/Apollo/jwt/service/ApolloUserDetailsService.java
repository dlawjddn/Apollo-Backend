package com.Teletubbies.Apollo.jwt.service;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.repository.UserRepository;
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

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApolloUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final ApolloUserTokenRepository apolloUserTokenRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        log.info("ApolloUserDeatailService 진입 성공");
        /*
        Optional<ApolloUser> findUser = userRepository.findByLogin(userLogin);
        log.info("find-user login: " + findUser.get().getId());
        log.info("find-user login: " + findUser.get().getLogin());
        ApolloUserToken apolloUserToken = changeTokenToUser(findUser);

         */

        Optional<ApolloUserToken> findUser = apolloUserTokenRepository.findByUserLogin(userLogin);

        //return Optional.ofNullable(apolloUserToken).map(this::createUserDetail).orElseThrow();

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
    public ApolloUserToken changeTokenToUser (Optional<ApolloUser> apolloUser){
        if (apolloUser != null && apolloUser.isPresent()){
            return new ApolloUserToken(apolloUser.get().getLogin(), apolloUser.get().getId().toString());
        } else throw new IllegalArgumentException("유저 정보가 없습니다");
    }
}
