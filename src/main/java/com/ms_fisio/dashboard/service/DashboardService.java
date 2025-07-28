package com.ms_fisio.dashboard.service;

import com.ms_fisio.dashboard.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.ms_fisio.session.domain.dto.FeedbackCommentaryDTO;
import com.ms_fisio.session.service.SessionService;

/**
 * Service for dashboard operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final SessionService sessionService;
    // private final NotificationService notificationService;
    
    /**
     * Get dashboard information including notifications and ongoing sessions
     */
    public DashboardInfoResponse getDashboardInfo(Long userId) {
        log.info("Fetching dashboard info for user: {}", userId);

        List<FeedbackCommentaryDTO> notifications = this.sessionService.getCommentsByRoutineCreator(userId);
        List<OngoingSessionDTO> ongoingSessions = this.sessionService.findOngoingSessionsByCreator(userId);
        
        return new DashboardInfoResponse(notifications, ongoingSessions);
    }
    
    /**
     * Get charts based on chart type preference
     */
    public ChartsResponse getCharts(String chartType, Long userId) {
        log.info("Generating charts of type: {} for user: {}", chartType, userId);
        
        List<ChartDto> charts = new ArrayList<>();
        
        if ("barras".equals(chartType)) {
            charts.add(new ChartDto(this.sessionService.getFeedbackSentimentStats(userId), "Satisfaccion de los Usuarios", "barras"));
        } else if ("lineas".equals(chartType)) {
            charts.add(new ChartDto(this.sessionService.getPlannedSessionsByStartDate(userId), "Sesiones Creadas", "lineas"));
            charts.add(new ChartDto(this.sessionService.getExecutedSessionsByStartDate(userId), "Sesiones Completadas", "lineas"));
        }
        
        return new ChartsResponse(charts);
    }
    
    /**
     * Mock notifications data
     */
    private List<NotificationDto> getMockNotifications() {
        List<NotificationDto> notifications = new ArrayList<>();
        
        String currentTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
        String pastTime = LocalDateTime.now().minusHours(2).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z";
        
        notifications.add(new NotificationDto("Sistema", currentTime, "Tu rutina ha sido actualizada."));
        notifications.add(new NotificationDto("Dr. García", pastTime, "Recuerda completar tu sesión de hoy."));
        notifications.add(new NotificationDto("Sistema", LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "Z", "Nueva rutina disponible."));
        
        return notifications;
    }
    
    /**
     * Mock ongoing sessions data
     */
    // private List<OngoingSessionDTO> getMockOngoingSessions() {
    //     List<OngoingSessionDTO> sessions = new ArrayList<>();
        
    //     sessions.add(new OngoingSessionDTO(
    //         "Luis", 
    //         "https://example.com/images/luis.jpg", 
    //         "Piernas Hardcore", 
    //         1
    //     ));
    //     sessions.add(new OngoingSessionDTO(
    //         "María", 
    //         "https://example.com/images/maria.jpg", 
    //         "Brazos Intensivo", 
    //         1
    //     ));
    //     sessions.add(new OngoingSessionDTO(
    //         "Carlos", 
    //         "https://example.com/images/carlos.jpg", 
    //         "Cardio Básico", 
    //         1
    //     ));
        
    //     return sessions;
    // }

}
