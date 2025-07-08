package com.ms_fisio.routine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for key moments in exercises
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyMomentDto {
    
    private String description;
    private String timestamp;
}
