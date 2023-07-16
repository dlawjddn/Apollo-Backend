package com.Teletubbies.Apollo.jwt.service;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        log.info("ApolloUserDeatailService 진입 성공");
        Optional<ApolloUser> findUser = userRepository.findByLogin(userLogin);
        log.info("find-user login: ", findUser.get().getLogin());
        return userRepository.findByLogin(userLogin)
                .map(this::createUserDetail)
                .orElseThrow();
    }
    public UserDetails createUserDetail(ApolloUser apolloUser){
        return User.builder()
                .username(apolloUser.getUsername())
                .password(passwordEncoder.encode(apolloUser.getPassword()))
                .build();
    }
}
