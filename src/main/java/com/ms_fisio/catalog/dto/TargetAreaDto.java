package com.ms_fisio.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for target area information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TargetAreaDto {
    
    private Long id;
    private String name;
}
