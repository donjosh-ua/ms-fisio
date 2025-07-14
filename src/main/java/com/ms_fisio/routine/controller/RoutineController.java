package com.ms_fisio.routine.controller;

import com.ms_fisio.routine.dto.*;
import com.ms_fisio.routine.domain.dto.CreateRoutineRequest;
import com.ms_fisio.routine.service.RoutineService;
import com.ms_fisio.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controller for routine management endpoints
 */
@RestController
@RequestMapping("/routines")
@RequiredArgsConstructor
@Validated
@Slf4j
public class RoutineController {
    
    private final RoutineService routineService;
    private final JwtService jwtService;
    
    /**
     * Create a new routine
     */
    @PostMapping
    public ResponseEntity<RoutineResponse> createRoutine(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody CreateRoutineRequest request) {
        
        log.info("Request to create routine: {}", request.getName());
        
        // Extract user ID from token or use mock user
        Long userId = getUserIdFromAuth(authorizationHeader);
        
        RoutineResponse response = routineService.createRoutine(request, userId);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Update an existing routine
     */
    @PutMapping("/{id}")
    public ResponseEntity<RoutineResponse> updateRoutine(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody CreateRoutineRequest request) {
        
        log.info("Request to update routine: {}", id);
        
        // Extract user ID from token or use mock user
        Long userId = getUserIdFromAuth(authorizationHeader);
        
        RoutineResponse response = routineService.updateRoutine(id, request, userId);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Delete a routine
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<RoutineResponse> deleteRoutine(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        
        log.info("Request to delete routine: {}", id);
        
        // Extract user ID from token or use mock user
        Long userId = getUserIdFromAuth(authorizationHeader);
        
        RoutineResponse response = routineService.deleteRoutine(id, userId);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Get all routines for the authenticated user
     */
    @GetMapping
    public ResponseEntity<RoutinesResponse> getAllRoutines(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        
        log.info("Request to get all routines");
        log.debug("Authorization header: {}", authorizationHeader);
        // Extract user ID from token or use mock user
        Long userId = getUserIdFromAuth(authorizationHeader);
        
        RoutinesResponse response = routineService.getAllRoutines(userId);
        return ResponseEntity.ok(response);
    }
    
    /**
     * Extract user ID from authorization header or return default
     */
    private Long getUserIdFromAuth(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring(7);
                return jwtService.extractUserId(token);
            } catch (Exception e) {
                log.warn("Failed to extract user ID from token: {}", e.getMessage());
            }
        }
        
        // Development mode: return default user ID
        log.debug("Using default user ID for development mode");
        return 1L;
    }
}
