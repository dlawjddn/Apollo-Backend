package com.Teletubbies.Apollo.core.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApolloExceptionHandler {
    @ExceptionHandler(ApolloException.class)
    protected ResponseEntity<ErrorResponseEntity> handleException(ApolloException e){
        return ErrorResponseEntity.toResponseEntity(e.getCode());
    }
}
