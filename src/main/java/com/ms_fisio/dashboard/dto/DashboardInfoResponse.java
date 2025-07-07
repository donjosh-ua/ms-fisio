package com.ms_fisio.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for dashboard information response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardInfoResponse {
    
    private List<NotificationDto> notifications;
    private List<OngoingSessionDto> ongoingSessions;
}
