package com.ms_fisio.dashboard.controller;

import com.ms_fisio.auth.service.JwtService;
import com.ms_fisio.dashboard.dto.ChartRequest;
import com.ms_fisio.dashboard.dto.ChartsResponse;
import com.ms_fisio.dashboard.dto.DashboardInfoResponse;
import com.ms_fisio.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controller for dashboard endpoints
 */
@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
@Validated
@Slf4j
public class DashboardController {
    
    private final DashboardService dashboardService;
    private final JwtService jwtService;
    
    /**
     * Get dashboard information
     */
    @GetMapping("/info")
    public ResponseEntity<DashboardInfoResponse> getDashboardInfo(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        
        Long userId = extractUserIdFromToken(authorizationHeader);
        DashboardInfoResponse response = dashboardService.getDashboardInfo(userId);
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Request analysis charts
     */
    @PostMapping("/charts")
    public ResponseEntity<ChartsResponse> getCharts(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody ChartRequest request) {
        
        Long userId = extractUserIdFromToken(authorizationHeader);
        ChartsResponse response = dashboardService.getCharts(request.getChartType(), userId);
        
        return ResponseEntity.ok(response);
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
