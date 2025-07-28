package com.ms_fisio.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.ms_fisio.session.domain.dto.FeedbackCommentaryDTO;

/**
 * DTO for dashboard information response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardInfoResponse {
    
    private List<FeedbackCommentaryDTO> notifications;
    private List<OngoingSessionDTO> ongoingSessions;
}
