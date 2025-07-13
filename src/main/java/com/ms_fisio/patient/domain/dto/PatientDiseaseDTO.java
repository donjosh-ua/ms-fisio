package com.ms_fisio.patient.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for patient disease association information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDiseaseDTO {
    private Long patientProfileId;
    private Long chronicDiseaseId;
    private String diseaseName;
}
