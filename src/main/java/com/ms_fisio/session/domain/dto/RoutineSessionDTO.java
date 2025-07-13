package com.ms_fisio.session.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.ms_fisio.routine.dto.RoutineSummaryDto;

import java.time.LocalDateTime;

/**
 * DTO for routine session response information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineSessionDTO {
    private Long routineSessionId;
    private String accessCode;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private boolean isActive; // Calculated field based on endDatetime
    private RoutineSummaryDto routine; // Include routine details
}
