package com.ms_fisio.patient.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new chronic disease
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateChronicDiseaseRequest {
    
    @NotBlank(message = "Disease name is required")
    @Size(max = 100, message = "Disease name must not exceed 100 characters")
    private String name;
}
