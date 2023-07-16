package com.Teletubbies.Apollo.auth.service;

import com.Teletubbies.Apollo.auth.domain.ApolloUser;
import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import com.Teletubbies.Apollo.auth.repository.UserRepository;
import com.Teletubbies.Apollo.core.exception.ApolloException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.Teletubbies.Apollo.core.exception.CustomErrorCode.DUPLICATED_USER_ERROR;
import static com.Teletubbies.Apollo.core.exception.CustomErrorCode.NOT_FOUND_USER_ERROR;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    @Transactional
    public Long saveUser(MemberInfoResponse memberInfoResponse) {
        ApolloUser apolloUserToSave = memberInfoResponse.changeDTOtoObj(memberInfoResponse);
        if (userRepository.existsById(apolloUserToSave.getId()))
            throw new ApolloException(DUPLICATED_USER_ERROR, "이미 존재하는 회원입니다");
        ApolloUser savedApolloUser = userRepository.save(memberInfoResponse.changeDTOtoObj(memberInfoResponse));
        return savedApolloUser.getId();
    }
    public ApolloUser getUserById(Long id){
        Optional<ApolloUser> optionalUser = userRepository.findById(id);
        if (optionalUser != null && optionalUser.isPresent()) return optionalUser.get();
        else throw new ApolloException(NOT_FOUND_USER_ERROR, "해당 ID에 맞는 회원이 없습니다");
    }
    public ApolloUser getUserByLogin(String login){
        Optional<ApolloUser> optionalUser = userRepository.findByLogin(login);
        if (optionalUser != null && optionalUser.isPresent()) return optionalUser.get();
        else throw new ApolloException(NOT_FOUND_USER_ERROR, "해당 loginID에 맞는 회원이 없습니다");
    }
    public ApolloUser getUserByName(String name){
        Optional<ApolloUser> optionalUser = userRepository.findByName(name);
        if (optionalUser != null && optionalUser.isPresent()) return optionalUser.get();
        else throw new ApolloException(NOT_FOUND_USER_ERROR, "해당 이름에 맞는 회원이 없습니다");
    }

}
