package com.ms_fisio.patient.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for affected zone information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffectedZoneDTO {
    private Long id;
    private String name;
}
