package com.ms_fisio.routine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ms_fisio.routine.domain.dto.RoutineDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard response DTO for routine operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineResponse {
    
    private boolean success;
    private String message;
    private String error;
    
    @JsonProperty("chueco_session_code")
    private String chuecoSessionCode;
    
    private RoutineDTO routine;
    
    public RoutineResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public RoutineResponse(boolean success, String message, RoutineDTO routine) {
        this.success = success;
        this.message = message;
        this.routine = routine;
    }
    
    public static RoutineResponse success(String message) {
        return new RoutineResponse(true, message);
    }
    
    public static RoutineResponse success(String message, String sessionCode) {
        RoutineResponse response = new RoutineResponse(true, message);
        response.setChuecoSessionCode(sessionCode);
        return response;
    }
    
    public static RoutineResponse success(String message, RoutineDTO routine) {
        return new RoutineResponse(true, message, routine);
    }
    
    public static RoutineResponse error(String error) {
        RoutineResponse response = new RoutineResponse();
        response.setSuccess(false);
        response.setError(error);
        return response;
    }
}
