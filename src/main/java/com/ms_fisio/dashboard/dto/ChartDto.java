package com.ms_fisio.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for chart information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartDto {
    
    private String type;
    private String source;
    private Integer year;
    
    // Constructor for charts without year (e.g., barras type)
    public ChartDto(String type, String source) {
        this.type = type;
        this.source = source;
    }
}
