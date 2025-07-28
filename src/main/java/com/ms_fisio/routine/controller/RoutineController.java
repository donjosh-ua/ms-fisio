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
import java.util.List;

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
        Long userId = getUserIdFromAuth(authorizationHeader);
        if (userId == null) {
            return ResponseEntity.status(401).body(RoutineResponse.error("Unauthorized"));
        }
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
        Long userId = getUserIdFromAuth(authorizationHeader);
        if (userId == null) {
            return ResponseEntity.status(401).body(RoutineResponse.error("Unauthorized"));
        }
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
        Long userId = getUserIdFromAuth(authorizationHeader);
        if (userId == null) {
            return ResponseEntity.status(401).body(RoutineResponse.error("Unauthorized"));
        }
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
        Long userId = getUserIdFromAuth(authorizationHeader);
        if (userId == null) {
            return ResponseEntity.status(401).body(new RoutinesResponse(List.of()));
        }
        RoutinesResponse response = routineService.getAllRoutines(userId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get a routine by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<RoutineResponse> getRoutineById(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        log.info("Request to get routine by id: {}", id);
        Long userId = getUserIdFromAuth(authorizationHeader);
        if (userId == null) {
            return ResponseEntity.status(401).body(RoutineResponse.error("Unauthorized"));
        }
        RoutineResponse response = routineService.getRoutineById(id, userId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    /**
     * Extract user ID from authorization header
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
        // No fallback: return null if no valid token
        return null;
    }
}
