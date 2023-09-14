package com.Teletubbies.Apollo.service;

import com.Teletubbies.Apollo.domain.ApolloUserToken;
import com.Teletubbies.Apollo.domain.UserAuthority;
import com.Teletubbies.Apollo.repository.ApolloUserTokenRepository;
import com.Teletubbies.Apollo.repository.UserAuthorityRepository;
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
    private final UserAuthorityRepository userAuthorityRepository;
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
        List<UserAuthority> allByApolloUserToken = userAuthorityRepository.findAllByApolloUserToken(apolloUserToken);

        List<GrantedAuthority> grantedAuthorities = allByApolloUserToken.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority().getAuthorityName()))
                .collect(Collectors.toList());
        for (GrantedAuthority grantedAuthority : grantedAuthorities) {
            log.info("해당 사용자의 권한 목록: "+grantedAuthority.getAuthority());
        }
        log.info("권한 정보 가져오기 성공");
        return new User(apolloUserToken.getUserLogin(), passwordEncoder.encode(apolloUserToken.getUserId()), grantedAuthorities);
    }
}
