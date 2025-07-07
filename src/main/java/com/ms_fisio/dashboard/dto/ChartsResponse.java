package com.ms_fisio.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for charts response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartsResponse {
    
    private List<ChartDto> charts;
}
