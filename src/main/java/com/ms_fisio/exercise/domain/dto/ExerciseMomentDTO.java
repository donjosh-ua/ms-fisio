package com.ms_fisio.exercise.domain.dto;

import lombok.Data;

@Data
public class ExerciseMomentDTO {
    private Long id;
    private String description;
    private Integer timestamp;
}
