package com.ms_fisio.session.service;

import com.ms_fisio.session.domain.dto.*;
import com.ms_fisio.session.domain.model.*;
import com.ms_fisio.session.repository.*;
import com.ms_fisio.dashboard.dto.OngoingSessionDTO;
import com.ms_fisio.routine.dto.RoutineSummaryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service for session operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final RoutineSessionRepository routineSessionRepository;
    private final FeedbackRepository feedbackRepository;

    @Value("${app.external.exercise-analysis.url:http://localhost:8082/api/exercise-analysis}")
    private String exerciseAnalysisUrl;

    /**
     * Get a session by access code (only if not expired)
     */
    @Transactional(readOnly = true)
    public SessionResponse getSessionByAccessCode(String accessCode) {
        try {
            log.info("Fetching session with access code: {}", accessCode);

            LocalDateTime now = LocalDateTime.now();
            RoutineSessionModel session = routineSessionRepository.findActiveByAccessCode(accessCode, now)
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Session not found or expired with access code: " + accessCode));

            RoutineSessionDTO sessionDTO = convertToDTO(session);

            return SessionResponse.success("Session retrieved successfully", sessionDTO);

        } catch (Exception e) {
            log.error("Error fetching session: {}", e.getMessage());
            return SessionResponse.error("Error retrieving session: " + e.getMessage());
        }
    }

    /**
     * Get all active sessions for a user (called from dashboard)
     */
    @Transactional(readOnly = true)
    public List<RoutineSessionDTO> getAllActiveSessionsForUser(Long userId) {
        try {
            log.info("Fetching all active sessions for user: {}", userId);

            LocalDateTime now = LocalDateTime.now();
            List<RoutineSessionModel> sessions = routineSessionRepository.findActiveSessionsByUserId(userId, now);

            return sessions.stream()
                    .map(this::convertToDTO)
                    .toList();

        } catch (Exception e) {
            log.error("Error fetching user sessions: {}", e.getMessage());
            return List.of();
        }
    }

    /**
     * Convert RoutineSessionModel to RoutineSessionDTO
     */
    private RoutineSessionDTO convertToDTO(RoutineSessionModel session) {
        RoutineSessionDTO dto = new RoutineSessionDTO();
        dto.setRoutineSessionId(session.getRoutineSessionId());
        dto.setAccessCode(session.getAccessCode());
        dto.setStartDatetime(session.getStartDatetime());
        dto.setEndDatetime(session.getEndDatetime());

        // Calculate if session is active
        boolean isActive = session.getEndDatetime() == null || session.getEndDatetime().isAfter(LocalDateTime.now());
        dto.setActive(isActive);

        // Convert routine to RoutineSummaryDto
        if (session.getRoutine() != null) {
            RoutineSummaryDto routineDto = new RoutineSummaryDto();
            routineDto.setId(session.getRoutine().getRoutineId().toString());
            routineDto.setName(session.getRoutine().getName());
            routineDto.setCategory(
                    session.getRoutine().getObjectiveArea() != null ? session.getRoutine().getObjectiveArea().getName()
                            : "");
            routineDto.setDuration(session.getRoutine().getDuration());
            routineDto.setDifficulty(session.getRoutine().getDifficulty());
            dto.setRoutine(routineDto);
        }

        return dto;
    }

    /**
     * Send feedback using access code
     */
    @Transactional
    public SessionResponse sendFeedback(SendFeedbackRequest request) {
        try {
            log.info("Sending feedback for access code: {}", request.getAccessCode());

            // Find session by access code (can be expired for feedback)
            RoutineSessionModel session = routineSessionRepository.findByAccessCode(request.getAccessCode())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Session not found with access code: " + request.getAccessCode()));

            // Create and save feedback
            FeedbackModel feedback = new FeedbackModel();
            feedback.setFeedback(request.getFeedback());
            feedback.setCalification(request.getCalification());
            feedback.setRoutineSession(session);

            feedback = feedbackRepository.save(feedback);
            log.info("Feedback saved with ID: {} and calification: {} for session: {}",
                    feedback.getFeedbackId(), feedback.getCalification(), session.getRoutineSessionId());

            return SessionResponse.success("Feedback sent successfully", null);

        } catch (Exception e) {
            log.error("Error sending feedback: {}", e.getMessage());
            return SessionResponse.error("Error sending feedback: " + e.getMessage());
        }
    }

    /**
     * Get exercise performance data (connects to external backend)
     */
    @Transactional(readOnly = true)
    public List<ExercisePerformanceDTO> getExercisePerformance(String accessCode) {
        try {
            log.info("Fetching exercise performance for access code: {}", accessCode);

            // Find session by access code
            RoutineSessionModel session = routineSessionRepository.findByAccessCode(accessCode)
                    .orElseThrow(
                            () -> new IllegalArgumentException("Session not found with access code: " + accessCode));

            // For now, return mock data - later this will call external backend
            List<ExercisePerformanceDTO> mockPerformanceData = createMockExercisePerformance(
                    session.getRoutineSessionId());

            // Future implementation will replace mock data with actual external API call:
            // String url = exerciseAnalysisUrl + "/performance/" + accessCode;
            // ExercisePerformanceDTO[] response = restTemplate.getForObject(url,
            // ExercisePerformanceDTO[].class);
            // return Arrays.asList(response);

            log.info("Retrieved {} exercise performance records for session: {}",
                    mockPerformanceData.size(), session.getRoutineSessionId());

            return mockPerformanceData;

        } catch (Exception e) {
            log.error("Error fetching exercise performance: {}", e.getMessage());
            return List.of();
        }
    }

    /**
     * Create mock exercise performance data for testing
     */
    private List<ExercisePerformanceDTO> createMockExercisePerformance(Long sessionId) {
        return List.of(
                new ExercisePerformanceDTO(
                        1L,
                        85.5f,
                        92.0f,
                        "capture1.jpg,capture2.jpg",
                        "GOOD",
                        101L,
                        "Knee Flexion Exercise",
                        sessionId),
                new ExercisePerformanceDTO(
                        2L,
                        78.2f,
                        88.5f,
                        "capture3.jpg,capture4.jpg",
                        "FAIR",
                        102L,
                        "Leg Extension",
                        sessionId),
                new ExercisePerformanceDTO(
                        3L,
                        94.1f,
                        96.8f,
                        "capture5.jpg,capture6.jpg",
                        "EXCELLENT",
                        103L,
                        "Balance Training",
                        sessionId));
    }

    @Transactional(readOnly = true)
    public Map<Integer, Map<Integer, Long>> getPlannedSessionsByStartDate(Long userId) {
        return getSessionChartGroupedByDate(
            "planned",
            routineSessionRepository.countPlannedSessionsByStartDate(userId)
        );
    }

    @Transactional(readOnly = true)
    public Map<Integer, Map<Integer, Long>> getExecutedSessionsByStartDate(Long userId) {
        LocalDateTime now = LocalDateTime.now();
        return getSessionChartGroupedByDate(
            "executed",
            routineSessionRepository.countExecutedSessionsByStartDate(userId, now)
        );
    }

    private Map<Integer, Map<Integer, Long>> getSessionChartGroupedByDate(String type, List<SessionChartDTO> data) {
        log.info("Fetching {} sessions grouped by year and month", type);

        return data.stream().collect(
            LinkedHashMap::new,
            (map, dto) -> map
                .computeIfAbsent(dto.getYear(), y -> new LinkedHashMap<>())
                .put(dto.getMonth(), dto.getCount()),
            LinkedHashMap::putAll
        );
    }

    @Transactional(readOnly = true)
    public Map<Integer, Map<Integer, Long>> getFeedbackSentimentStats(Long userId) {
        List<BarChartDTO> results = feedbackRepository.countFeedbackBySentiment(userId);

        // Convertimos la lista a Map<calification, count>
        Map<Integer, Long> sentimentMap = results.stream()
            .collect(Collectors.toMap(
                BarChartDTO::getCalification, // sentimiento como 1, 2, 3
                BarChartDTO::getCount,
                Long::sum,
                LinkedHashMap::new
            ));

        // Lo retornamos dentro de otro Map con clave fija, como hacen otros charts
        Map<Integer, Map<Integer, Long>> result = new LinkedHashMap<>();
        result.put(0, sentimentMap);

        return result;
    }



    @Transactional(readOnly = true)
    public List<OngoingSessionDTO> findOngoingSessionsByCreator(Long userId) {
        return routineSessionRepository.findOngoingSessionsByCreator(userId);
    }

    @Transactional(readOnly = true)
    public List<FeedbackCommentaryDTO> getCommentsByRoutineCreator(Long userId) {
        return feedbackRepository.findCommentsByRoutineCreator(userId);
    }
}
