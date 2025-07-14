package com.ms_fisio.session.controller;

import com.ms_fisio.auth.service.JwtService;
import com.ms_fisio.session.domain.dto.*;
import com.ms_fisio.session.service.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

/**
 * Controller for session endpoints
 */
@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
@Validated
@Slf4j
public class SessionController {
    
    private final SessionService sessionService;
    private final JwtService jwtService;
    
    /**
     * Get session by access code (only if not expired)
     * Used by patients to access their sessions
     */
    @GetMapping("/access/{accessCode}")
    public ResponseEntity<SessionResponse> getSessionByAccessCode(@PathVariable String accessCode) {
        log.info("Getting session by access code: {}", accessCode);
        
        SessionResponse response = sessionService.getSessionByAccessCode(accessCode);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get all active sessions for authenticated user (called from dashboard)
     * Used by physiotherapists to see all their active sessions
     */
    @GetMapping("/active")
    public ResponseEntity<List<RoutineSessionDTO>> getActiveSessionsForUser(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        
        Long userId = extractUserIdFromToken(authorizationHeader);
        log.info("Getting active sessions for user: {}", userId);
        
        List<RoutineSessionDTO> sessions = sessionService.getAllActiveSessionsForUser(userId);
        
        return ResponseEntity.ok(sessions);
    }
    
    /**
     * Send feedback using access code
     * Finds session by access code and saves feedback
     */
    @PostMapping("/feedback")
    public ResponseEntity<SessionResponse> sendFeedback(@Valid @RequestBody SendFeedbackRequest request) {
        log.info("Sending feedback for access code: {}", request.getAccessCode());
        
        SessionResponse response = sessionService.sendFeedback(request);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get exercise performance data by access code
     * Will connect to external backend for exercise analysis
     */
    @GetMapping("/performance/{accessCode}")
    public ResponseEntity<List<ExercisePerformanceDTO>> getExercisePerformance(@PathVariable String accessCode) {
        log.info("Getting exercise performance for access code: {}", accessCode);
        
        List<ExercisePerformanceDTO> performance = sessionService.getExercisePerformance(accessCode);
        
        return ResponseEntity.ok(performance);
    }

    /**
     * Extract user ID from JWT token (mock for development)
     */
    private Long extractUserIdFromToken(String authorizationHeader) {
        // For development/testing, return mock user ID
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            return 1L; // Default test user ID
        }
        
        try {
            String token = authorizationHeader.replace("Bearer ", "");
            return jwtService.extractUserId(token);
        } catch (Exception e) {
            log.warn("Failed to extract user ID from token, using default: {}", e.getMessage());
            return 1L; // Fallback to test user ID
        }
    }
}
