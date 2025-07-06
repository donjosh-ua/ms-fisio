package com.ms_fisio.auth.exception;

/**
 * Exception thrown when Google authentication fails
 */
public class GoogleAuthenticationException extends RuntimeException {
    
    public GoogleAuthenticationException(String message) {
        super(message);
    }
    
    public GoogleAuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
