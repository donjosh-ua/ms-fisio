package com.ms_fisio.dashboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for chart request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartRequest {
    
    @NotBlank(message = "Chart type is required")
    @Pattern(regexp = "barras|lineas", message = "Chart type must be 'barras' or 'lineas'")
    private String chartType;
}
