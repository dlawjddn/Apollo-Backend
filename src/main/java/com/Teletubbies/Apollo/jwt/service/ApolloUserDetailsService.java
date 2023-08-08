package com.Teletubbies.Apollo.jwt.service;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.jwt.domain.ApolloUserToken;
import com.Teletubbies.Apollo.jwt.repository.ApolloUserTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApolloUserDetailsService implements UserDetailsService {
    private final ApolloUserTokenRepository apolloUserTokenRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String userLogin) throws UsernameNotFoundException {
        log.info("ApolloUserDetailsService 진입 성공");
        return apolloUserTokenRepository.findByUserLogin(userLogin)
                .map(apolloUserToken -> createUserDetail(userLogin, apolloUserToken))
                .orElseThrow();

    }
    private User createUserDetail(String id, ApolloUserToken apolloUserToken){
        log.info("createUser 진입 성공");
        if (!apolloUserToken.isActivated())
            throw new RuntimeException(id + "-> 활성화 되지 않은 사용자입니다.");

        List<GrantedAuthority> grantedAuthorities = apolloUserToken.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());
        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            System.out.println(grantedAuthority.getAuthority());
        }
        log.info("권한 정보 가져오기 성공");
        return new User(apolloUserToken.getUserLogin(), passwordEncoder.encode(apolloUserToken.getUserId()), grantedAuthorities);
    }
}
