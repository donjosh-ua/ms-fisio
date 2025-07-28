package com.ms_fisio.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for ongoing session information in dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OngoingSessionDTO {
    
    private String patientName;
    private String patientPfpUrl;
    private String routineName;
    private Long routineId;
}
