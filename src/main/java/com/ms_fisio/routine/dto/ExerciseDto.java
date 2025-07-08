package com.ms_fisio.routine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for exercise information in routines
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseDto {
    
    private String name;
    
    @JsonProperty("video_url")
    private String videoUrl;
    
    @JsonProperty("video_id")
    private String videoId;
    
    private Integer sets;
    private Integer repetitions;
    private Boolean assisted;
    private String description;
    
    @JsonProperty("key_moments")
    private List<KeyMomentDto> keyMoments;
}
