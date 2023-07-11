package com.Teletubbies.Apollo.jwt.test.dto;

import lombok.Data;

@Data
public class MemberLoginRequestDto {
    private String memberId;
    private String password;
}
