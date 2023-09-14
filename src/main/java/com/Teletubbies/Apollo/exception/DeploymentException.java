package com.Teletubbies.Apollo.exception;

import com.Teletubbies.Apollo.exception.ApolloException;
import com.Teletubbies.Apollo.exception.CustomErrorCode;

public class DeploymentException extends ApolloException {
    public DeploymentException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
