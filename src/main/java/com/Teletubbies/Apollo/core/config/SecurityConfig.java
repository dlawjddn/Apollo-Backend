package com.Teletubbies.Apollo.core.config;

import com.Teletubbies.Apollo.jwt.JwtAuthenticationFilter;
import com.Teletubbies.Apollo.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                // csrf disable
                .csrf(AbstractHttpConfigurer::disable)
                .cors((cors) -> configurationSource())
                // session 사용하지 않을 듯!
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //form login 사용하지 않겠다.
                .formLogin((formLogin) -> formLogin.disable())
                .httpBasic((httpBasic) -> httpBasic.disable())
                //모든 http 요청에 대한 허용
                .authorizeHttpRequests((authorizeRequests) ->
                        authorizeRequests.anyRequest().permitAll())
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    public CorsConfigurationSource configurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*"); // http header 모두 허용
        configuration.addAllowedMethod("*"); // http method 모두 허용
        configuration.addAllowedOriginPattern("*"); // 모든 ip 주소 허용
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /*
    1. httpBasic().disable().csrf().disable():
        rest api이므로 basic auth 및 csrf 보안을 사용하지 않는다는 설정이다.

    2. sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS):
        JWT를 사용하기 때문에 세션을 사용하지 않는다는 설정이다.

    3. antMatchers().permitAll():
        해당 API에 대해서는 모든 요청을 허가한다는 설정이다.

    5. anyRequest().authenticated():
        이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정이다.

    6. addFilterBefore(new JwtAUthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class):
        JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행하겠다는 설정이다.

    7. passwordEncoder:
        JWT를 사용하기 위해서는 기본적으로 password encoder가 필요한데, 여기서는 Bycrypt encoder를 사용했다.
     */
}
