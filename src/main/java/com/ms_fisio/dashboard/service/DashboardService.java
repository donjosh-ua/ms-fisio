package com.ms_fisio.dashboard.service;

import com.ms_fisio.dashboard.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for dashboard operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {
    
    /**
     * Get dashboard information including notifications and ongoing sessions
     */
    public DashboardInfoResponse getDashboardInfo(Long userId) {
        log.info("Fetching dashboard info for user: {}", userId);
        
        List<NotificationDto> notifications = getMockNotifications();
        List<OngoingSessionDto> ongoingSessions = getMockOngoingSessions();
        
        return new DashboardInfoResponse(notifications, ongoingSessions);
    }
    
    /**
     * Get charts based on chart type preference
     */
    public ChartsResponse getCharts(String chartType, Long userId) {
        log.info("Generating charts of type: {} for user: {}", chartType, userId);
        
        List<ChartDto> charts = new ArrayList<>();
        
        if ("barras".equals(chartType)) {
            charts.add(new ChartDto("barras", "https://example.com/charts/barras_weekly.png"));
            charts.add(new ChartDto("barras", "https://example.com/charts/barras_monthly.png"));
        } else if ("lineas".equals(chartType)) {
            charts.add(new ChartDto("completadas", "https://example.com/charts/completadas_2024.png", 2024));
            charts.add(new ChartDto("planificadas", "https://example.com/charts/planificadas_2024.png", 2024));
            charts.add(new ChartDto("completadas", "https://example.com/charts/completadas_2023.png", 2023));
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
    private List<OngoingSessionDto> getMockOngoingSessions() {
        List<OngoingSessionDto> sessions = new ArrayList<>();
        
        sessions.add(new OngoingSessionDto(
            "Luis", 
            "https://example.com/images/luis.jpg", 
            "Piernas Hardcore", 
            "abc123"
        ));
        sessions.add(new OngoingSessionDto(
            "María", 
            "https://example.com/images/maria.jpg", 
            "Brazos Intensivo", 
            "def456"
        ));
        sessions.add(new OngoingSessionDto(
            "Carlos", 
            "https://example.com/images/carlos.jpg", 
            "Cardio Básico", 
            "ghi789"
        ));
        
        return sessions;
    }
}
