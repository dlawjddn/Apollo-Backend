package com.Teletubbies.Apollo.exception;

import com.Teletubbies.Apollo.exception.ApolloException;
import com.Teletubbies.Apollo.exception.CustomErrorCode;

public class CredentialException extends ApolloException {
    public CredentialException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
