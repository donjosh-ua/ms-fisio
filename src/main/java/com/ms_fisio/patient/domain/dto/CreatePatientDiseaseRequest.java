package com.ms_fisio.patient.domain.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a patient disease association
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientDiseaseRequest {
    
    @NotNull(message = "Patient profile ID is required")
    private Long patientProfileId;
    
    @NotNull(message = "Chronic disease ID is required")
    private Long chronicDiseaseId;
}
