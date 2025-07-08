package com.ms_fisio.routine.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for creating or updating a routine
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoutineRequest {
    
    @NotBlank(message = "Routine name is required")
    private String name;
    
    private String category;
    
    private String description;
    
    @NotBlank(message = "Difficulty is required")
    private String difficulty;
    
    private Boolean favorite = false;
    
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer duration;
    
    @NotNull(message = "Target area ID is required")
    private Long targetAreaId;
    
    @Min(value = 1, message = "Weeks must be at least 1")
    private Integer weeks;
    
    private List<String> days;
    
    @Valid
    private List<ExerciseDto> exercises;
}
