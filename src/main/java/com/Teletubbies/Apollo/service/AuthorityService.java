package com.Teletubbies.Apollo.service;

import com.Teletubbies.Apollo.domain.Authority;
import com.Teletubbies.Apollo.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@RequiredArgsConstructor
public class AuthorityService {
    private final AuthorityRepository authorityRepository;
    @Transactional
    public void saveAuthority(String auth){
        authorityRepository.save(new Authority(auth));
    }
    public Authority findByName(String authorityName){
        return authorityRepository.findByAuthorityName(authorityName);
    }
    public boolean existAuthority(Long id){
        return authorityRepository.existsById(id);
    }
}
