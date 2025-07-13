package com.ms_fisio.patient.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for patient profile response information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileDTO {
    private Long patientProfileId;
    private String fullName;
    private String idNumber;
    private String phone;
    private Integer age;
    private String sex;
    private String email;
    private String priorSurgeries;
    private LocalDate painDate;
    private Integer painLevel;
    private String diagnosis;
    private Long userId;
    
    // Associated data
    private List<ChronicDiseaseDTO> chronicDiseases;
    private List<AffectedZoneDTO> affectedZones;
    private List<LesionTypeDTO> lesionTypes;
    private List<PatientRoutineDTO> assignedRoutines;
}
