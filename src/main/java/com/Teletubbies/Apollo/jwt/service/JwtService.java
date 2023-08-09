package com.Teletubbies.Apollo.jwt.service;

import com.Teletubbies.Apollo.jwt.JwtTokenProvider;
import com.Teletubbies.Apollo.jwt.domain.ApolloUserToken;
import com.Teletubbies.Apollo.jwt.domain.Authority;
import com.Teletubbies.Apollo.jwt.domain.Token;
import com.Teletubbies.Apollo.jwt.domain.UserAuthority;
import com.Teletubbies.Apollo.jwt.dto.TokenInfo;
import com.Teletubbies.Apollo.jwt.repository.ApolloUserTokenRepository;
import com.Teletubbies.Apollo.jwt.repository.UserAuthorityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    private final ApolloUserTokenRepository apolloUserTokenRepository;
    private final UserAuthorityRepository userAuthorityRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    public ApolloUserToken toMakeTokenSaveObj(String userLogin, String userId){
        return apolloUserTokenRepository.save(new ApolloUserToken(userLogin, userId));
    }
    @Transactional
    public UserAuthority saveUserAuthority(ApolloUserToken userToken, Authority authority){
        return userAuthorityRepository.save(new UserAuthority(userToken, authority));
    }
    public TokenInfo login(String userLogin, String userId){
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userLogin, userId);
        log.info("authenticationtoken name: " + authenticationToken.getName());
        log.info("authenticationtoken principal: " +authenticationToken.getPrincipal().toString());
        log.info("authenticationtoken credentials " +authenticationToken.getCredentials().toString());
        log.info("authenticationtoken Authorities: " +authenticationToken.getAuthorities().toString());
        log.info("Authentication 객체 생성 완료");
        // getDetail = null, getAuthentirities = 없음

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 ApolloUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("실제 검증 성공");

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
        log.info("jwt 토큰 생성 완료");
        log.info("token GrantType: "+tokenInfo.getGrantType());
        log.info("token access-token: "+tokenInfo.getAccessToken());
        log.info("token refresh-token: "+tokenInfo.getRefreshToken());

        tokenService.saveToken(new Token(userId, tokenInfo));

        return tokenInfo;
    }
}
가