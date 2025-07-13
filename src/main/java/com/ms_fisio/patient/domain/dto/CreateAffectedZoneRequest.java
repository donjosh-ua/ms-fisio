package com.ms_fisio.patient.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for creating a new affected zone
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAffectedZoneRequest {
    
    @NotBlank(message = "Zone name is required")
    @Size(max = 100, message = "Zone name must not exceed 100 characters")
    private String name;
}
