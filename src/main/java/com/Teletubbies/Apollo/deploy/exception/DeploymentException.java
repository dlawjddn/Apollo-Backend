package com.Teletubbies.Apollo.deploy.exception;

import com.Teletubbies.Apollo.core.exception.ApolloException;
import com.Teletubbies.Apollo.core.exception.CustomErrorCode;

public class DeploymentException extends ApolloException {
    public DeploymentException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
