package com.ms_fisio.patient.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a patient lesion association
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientLesionRequest {
    
    @NotNull(message = "Patient profile ID is required")
    private Long patientProfileId;
    
    @NotNull(message = "Lesion type ID is required")
    private Long lesionTypeId;
}
