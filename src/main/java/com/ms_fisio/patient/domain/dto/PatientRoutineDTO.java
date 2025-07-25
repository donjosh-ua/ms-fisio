package com.ms_fisio.patient.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for patient routine assignment information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRoutineDTO {
    private Long patientProfileId;
    private Long routineSessionId;
    private String routineName;
    private String routineDescription;
    private Integer duration;
    private String difficulty;
}
