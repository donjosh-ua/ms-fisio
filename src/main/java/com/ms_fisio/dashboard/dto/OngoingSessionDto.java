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
public class OngoingSessionDto {
    
    private String chuecoName;
    private String chuecoPfpUrl;
    private String routineName;
    private String routineId;
}
