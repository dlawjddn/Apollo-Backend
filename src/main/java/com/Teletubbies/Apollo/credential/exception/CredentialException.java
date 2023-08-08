package com.Teletubbies.Apollo.credential.exception;

import com.Teletubbies.Apollo.core.exception.ApolloException;
import com.Teletubbies.Apollo.core.exception.CustomErrorCode;

public class CredentialException extends ApolloException {
    public CredentialException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
