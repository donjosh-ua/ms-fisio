package com.ms_fisio.patient.domain.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO for creating or updating patient profile
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileRequest {
    
    private Long id; // For editing existing profile
    
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;
    
    @NotBlank(message = "ID number is required")
    @Size(max = 50, message = "ID number must not exceed 50 characters")
    private String idNumber;
    
    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;
    
    @Min(value = 1, message = "Age must be at least 1")
    @Max(value = 150, message = "Age must not exceed 150")
    private Integer age;
    
    @Size(max = 10, message = "Sex must not exceed 10 characters")
    private String sex;
    
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;
    
    @Size(max = 1000, message = "Prior surgeries description must not exceed 1000 characters")
    private String priorSurgeries;
    
    private List<Long> chronicDiseaseIds;
    
    private List<Long> affectedZoneIds;
    
    private List<Long> lesionTypeIds;
    
    private LocalDate painStartDate;
    
    @Min(value = 0, message = "Pain level must be at least 0")
    @Max(value = 10, message = "Pain level must not exceed 10")
    private Integer painLevel;
    
    @Size(max = 1000, message = "Medical diagnosis must not exceed 1000 characters")
    private String medicalDiagnosis;
    
    @NotNull(message = "Routine ID is required")
    private Long routineId;
}
