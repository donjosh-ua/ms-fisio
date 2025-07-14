package com.ms_fisio.session.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for exercise performance data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExercisePerformanceDTO {
    private Long exercisePerformanceId;
    private Float postureCompliance;
    private Float repetitionCompliance;
    private String imageCaptures;
    private String complianceLevel;
    private Long exerciseId;
    private String exerciseName;
    private Long sessionId;
}