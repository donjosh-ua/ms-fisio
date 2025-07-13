package com.ms_fisio.patient.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for patient affected zone association information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientZoneDTO {
    private Long patientProfileId;
    private Long affectedZoneId;
    private String zoneName;
}
