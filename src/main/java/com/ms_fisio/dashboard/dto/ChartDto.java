package com.ms_fisio.dashboard.dto;

import java.util.Map;

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
    private Map<Integer, Map<Integer, Long>> data;
    private String title;
    private String type;
}
