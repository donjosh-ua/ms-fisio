package com.ms_fisio.session.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * API response DTO for session operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionResponse {
    private boolean success;
    private String message;
    private RoutineSessionDTO session;
    
    public SessionResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static SessionResponse success(String message, RoutineSessionDTO session) {
        return new SessionResponse(true, message, session);
    }
    
    public static SessionResponse error(String message) {
        return new SessionResponse(false, message);
    }
}
