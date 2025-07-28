package com.ms_fisio.account.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard response DTO for account operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    
    private boolean success;
    private String message;
    private String error;
    private Object data;
    
    public AccountResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public AccountResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    public static AccountResponse success(String message) {
        return new AccountResponse(true, message);
    }
    
    public static AccountResponse success(String message, Object data) {
        return new AccountResponse(true, message, data);
    }
    
    public static AccountResponse error(String error) {
        AccountResponse response = new AccountResponse();
        response.setSuccess(false);
        response.setError(error);
        return response;
    }
}
