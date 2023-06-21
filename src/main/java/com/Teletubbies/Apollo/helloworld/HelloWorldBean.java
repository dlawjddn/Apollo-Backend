package com.Teletubbies.Apollo.helloworld;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HelloWorldBean {
    private String message;

    public HelloWorldBean(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("HelloWorldBean [message=%s]", message);
    }
}
