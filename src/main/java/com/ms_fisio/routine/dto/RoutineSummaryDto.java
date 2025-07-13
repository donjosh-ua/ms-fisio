package com.ms_fisio.routine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for routine summary information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineSummaryDto {
    
    private String id;
    private String name;
    private String category;
    private Integer duration;
    private Integer difficulty;
}
