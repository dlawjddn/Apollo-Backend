package com.Teletubbies.Apollo.exception;

import com.Teletubbies.Apollo.exception.ApolloException;
import com.Teletubbies.Apollo.exception.CustomErrorCode;

public class MonitorException extends ApolloException {
    public MonitorException(CustomErrorCode code, String logMessage) {
        super(code, logMessage);
    }
}
