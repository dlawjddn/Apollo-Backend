package com.Teletubbies.Apollo.jwt.controller;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.service.UserService;
import com.Teletubbies.Apollo.jwt.dto.UserLoginRequest;
import com.Teletubbies.Apollo.jwt.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Jwt Controller", description = "jwt 이용 login api")
public class JwtController {
    private final UserService userService;
    private final JwtService jwtService;
    @Operation(summary = "jwt login api", description = "회원 가입을 한 상태여야 진행이 가능함, username = login, password = userId")
    @PostMapping("/login/user")
    public HashMap<String, Object> login(@RequestBody UserLoginRequest userLoginRequest){
        String userLogin = userLoginRequest.getUserLogin();
        String userId = userLoginRequest.getUserId();
        log.info("user login: " + userLogin);
        log.info("user id: " + userId);
        HashMap<String, Object> resultJSON = new HashMap<>();

        Optional<ApolloUser> loginUser = userService.findByLoginNullAble(userLogin);
        // 회원가입 하지 않은 유저, 회원 저장로직 요청을 프론트에게 다시 해달라고 함
        if (loginUser.isEmpty()){
            resultJSON.put("result", "we have to save user");
            return resultJSON;
        }
        //회원가입을 한 유저, token 발행
        resultJSON.put("result", jwtService.login(userLogin, userId));
        return resultJSON;
    }
}
