package com.ms_fisio.patient.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for lesion type information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LesionTypeDTO {
    private Long id;
    private String name;
}
