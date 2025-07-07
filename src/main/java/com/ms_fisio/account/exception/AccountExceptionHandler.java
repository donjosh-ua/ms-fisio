package com.ms_fisio.account.exception;

import com.ms_fisio.account.dto.AccountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for account-related exceptions
 */
@RestControllerAdvice
@Slf4j
public class AccountExceptionHandler {
    
    /**
     * Handle account exceptions
     */
    @ExceptionHandler(AccountException.class)
    public ResponseEntity<AccountResponse> handleAccountException(AccountException e) {
        log.error("Account error: {}", e.getMessage());
        return ResponseEntity.badRequest().body(AccountResponse.error(e.getMessage()));
    }
    
    /**
     * Handle validation errors for account endpoints
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AccountResponse> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        String errorMessage = errors.values().iterator().next(); // Get first validation error
        log.error("Validation error: {}", errorMessage);
        
        return ResponseEntity.badRequest().body(AccountResponse.error(errorMessage));
    }
}
