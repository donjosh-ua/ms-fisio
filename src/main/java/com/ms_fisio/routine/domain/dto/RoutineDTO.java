package com.ms_fisio.routine.domain.dto;

import lombok.Data;
import java.util.List;
import com.ms_fisio.exercise.domain.dto.ObjectiveAreaDTO;
import com.ms_fisio.exercise.domain.dto.ExerciseDTO;

@Data
public class RoutineDTO {
    private Long routineId;
    private String name;
    private String category;
    private String description;
    private Integer difficulty;
    private Integer duration;
    private Integer weeks;
    private ObjectiveAreaDTO objectiveArea;
    private List<String> days;
    private List<ExerciseDTO> exercises;
}
