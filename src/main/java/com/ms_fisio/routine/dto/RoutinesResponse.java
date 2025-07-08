package com.ms_fisio.routine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for getting all routines response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutinesResponse {
    
    private List<RoutineSummaryDto> routines;
}
