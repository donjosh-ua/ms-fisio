package com.ms_fisio.catalog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for target areas catalog response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TargetAreasResponse {
    
    @JsonProperty("target_areas")
    private List<TargetAreaDto> targetAreas;
}
