package com.ms_fisio.patient.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Enhanced API response with session ID for patient profile creation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileResponse {
    private boolean success;
    private String message;
    private Long patientProfileId;
    private Long sessionId;
    private String accessCode;
    
    public PatientProfileResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public static PatientProfileResponse success(String message, Long patientProfileId, Long sessionId, String accessCode) {
        return new PatientProfileResponse(true, message, patientProfileId, sessionId, accessCode);
    }
    
    public static PatientProfileResponse error(String message) {
        return new PatientProfileResponse(false, message);
    }
}
