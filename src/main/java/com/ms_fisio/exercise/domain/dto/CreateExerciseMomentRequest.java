package com.ms_fisio.exercise.domain.dto;

import lombok.Data;

@Data
public class CreateExerciseMomentRequest {
    private String description;
    private Integer timestamp;
    private Long exerciseId;
}
