package com.Teletubbies.Apollo.core.exception;

import lombok.Getter;

@Getter
public class ApolloException extends RuntimeException {
    private final CustomErrorCode code;

    public ApolloException(CustomErrorCode code, String logMessage) {
        super(logMessage);
        this.code = code;
    }
}
