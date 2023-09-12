package com.Teletubbies.Apollo.core.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
@Builder
@Getter
public class ErrorResponseEntity {
    private int status;
    private String code;
    private String message;

    public static ResponseEntity<ErrorResponseEntity> toResponseEntity(CustomErrorCode e){
        return ResponseEntity
                .status(Integer.parseInt(e.getCodeNumber()))
                .body(ErrorResponseEntity.builder()
                        .status(Integer.parseInt(e.getCodeNumber())) // http code -> ex) 500, 400, 200 ...
                        .code(e.name()) // 내가 설정한 에러의 이름 ex) NOT_FOUND_MEMBER
                        .message(e.getMessage()) // 내가 설정한 에러의 설명 ex) "등록된 멤버가 없습니다"
                        .build()
                );
    }
}
