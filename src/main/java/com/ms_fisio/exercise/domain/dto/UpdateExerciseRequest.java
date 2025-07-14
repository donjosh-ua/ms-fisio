package com.ms_fisio.exercise.domain.dto;

import lombok.Data;
import java.util.List;

@Data
public class UpdateExerciseRequest {
    private Long exerciseId;
    private String name;
    private String description;
    private String videoUrl;
    private String videoId;
    private Integer sets;
    private Integer repetitions;
    private Boolean assisted;
    private List<ExerciseMomentDTO> keyMoments;
    private Long objectiveAreaId;
}
