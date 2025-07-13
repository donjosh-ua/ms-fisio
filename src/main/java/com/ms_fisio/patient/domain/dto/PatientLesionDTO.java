package com.ms_fisio.patient.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for patient lesion type association information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientLesionDTO {
    private Long patientProfileId;
    private Long lesionTypeId;
    private String lesionTypeName;
}
