package com.ms_fisio.patient.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for deleting routine assignment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteRoutineRequest {
    
    @NotNull(message = "Patient profile ID is required")
    private Long patientProfileId;
    
    @NotNull(message = "Routine session ID is required")
    private Long routineSessionId;
}
