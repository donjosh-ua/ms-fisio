package com.ms_fisio.account.exception;

/**
 * Exception thrown when account operation fails
 */
public class AccountException extends RuntimeException {
    
    public AccountException(String message) {
        super(message);
    }
    
    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
