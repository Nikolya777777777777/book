package com.example.demo.exception;

public class BadCredentialException extends RuntimeException {
    public BadCredentialException(String message) {
        super(message);
    }

    public BadCredentialException(String message, RuntimeException e) {
        super(message, e);
    }
}
