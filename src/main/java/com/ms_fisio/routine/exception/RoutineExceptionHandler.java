package com.ms_fisio.routine.exception;

import com.ms_fisio.routine.dto.RoutineResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for routine-related exceptions
 */
@RestControllerAdvice
@Slf4j
public class RoutineExceptionHandler {
    
    /**
     * Handle validation errors for routine endpoints
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RoutineResponse> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        String errorMessage = errors.values().iterator().next();
        log.error("Routine validation error: {}", errorMessage);
        return ResponseEntity.badRequest().body(RoutineResponse.error("Validation error: " + errorMessage));
    }
    
    /**
     * Handle general exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RoutineResponse> handleGeneralException(Exception e) {
        log.error("Unexpected error in routine operation: {}", e.getMessage());
        return ResponseEntity.internalServerError()
                .body(RoutineResponse.error("An unexpected error occurred"));
    }
}
