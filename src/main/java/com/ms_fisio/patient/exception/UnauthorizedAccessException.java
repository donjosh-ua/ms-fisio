package com.ms_fisio.patient.exception;

/**
 * Exception thrown when a user tries to access a resource they don't own
 */
public class UnauthorizedAccessException extends RuntimeException {
    
    public UnauthorizedAccessException(String message) {
        super(message);
    }
    
    public UnauthorizedAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
