package com.Teletubbies.Apollo.auth.service;

import com.Teletubbies.Apollo.auth.domain.User;
import com.Teletubbies.Apollo.auth.dto.MemberInfoResponse;
import com.Teletubbies.Apollo.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    public void saveUser(MemberInfoResponse memberInfoResponse) {
        userRepository.save(memberInfoResponse.changeDTOtoObj(memberInfoResponse));
    }
    public User getUserById(Long id){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser != null && optionalUser.isPresent()) return optionalUser.get();
        else throw new IllegalArgumentException("유효하지 않은 회원입니다!");
    }
    public User getUserByLogin(String login){
        Optional<User> optionalUser = userRepository.findByLogin(login);
        if (optionalUser != null && optionalUser.isPresent()) return optionalUser.get();
        else throw new IllegalArgumentException("유효하지 않은 회원입니다!");
    }
    public User getUserByName(String name){
        Optional<User> optionalUser = userRepository.findByName(name);
        if (optionalUser != null && optionalUser.isPresent()) return optionalUser.get();
        else throw new IllegalArgumentException("유효하지 않은 회원입니다!");
    }

}
