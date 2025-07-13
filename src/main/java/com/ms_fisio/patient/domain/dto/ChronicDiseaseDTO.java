package com.ms_fisio.patient.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for chronic disease information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChronicDiseaseDTO {
    private Long id;
    private String name;
}
