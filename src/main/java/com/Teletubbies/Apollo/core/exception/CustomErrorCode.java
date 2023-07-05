package com.Teletubbies.Apollo.core.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum CustomErrorCode {
    EXPIRED_AUTHORIZATION_ERROR("0200", "이미 만료된 토큰입니다"),
    INVALID_AUTHORIZATION_ERROR("0201", "유효하지 않은 토큰입니다"),
    GITHUB_AUTHORIZATION_ERROR("0202", "깃허브 인증에 실패했습니다"),
    EMPTY_AUTHORIZATION_ERROR("0203", "인증 토큰이 존재하지 않습니다"),
    INVALID_USER_ERROR("0404", "존재하지 않는 회원입니다"),
    API_NOT_FOUND_ERROR("4300", "존재하지 않는 API입니다"),

    RUNTIME_ERROR("9902", "알 수 없는 예외가 발생했습니다"),
    APOLLO_ERROR("9901", "처리하지 못한 예외입니다");

    private final String codeNumber;
    private final String message;
}
