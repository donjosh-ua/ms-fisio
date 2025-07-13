package com.ms_fisio.patient.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a patient zone association
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientZoneRequest {
    
    @NotNull(message = "Patient profile ID is required")
    private Long patientProfileId;
    
    @NotNull(message = "Affected zone ID is required")
    private Long affectedZoneId;
}
