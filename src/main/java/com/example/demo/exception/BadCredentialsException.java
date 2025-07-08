package com.example.demo.exception;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String message) {
        super(message);
    }

    public BadCredentialsException(String message, Exception e) {
        super(message, e);
    }
}
