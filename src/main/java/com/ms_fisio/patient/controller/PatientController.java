package com.ms_fisio.patient.controller;

import com.ms_fisio.auth.service.JwtService;
import com.ms_fisio.patient.domain.dto.*;
import com.ms_fisio.patient.domain.dto.ChronicDiseaseDTO;
import com.ms_fisio.patient.domain.dto.AffectedZoneDTO;
import com.ms_fisio.patient.domain.dto.LesionTypeDTO;
import com.ms_fisio.patient.service.PatientService;
import com.ms_fisio.routine.dto.RoutinesResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * Controller for patient endpoints
 */
@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PatientController {

    private final PatientService patientService;
    private final JwtService jwtService;

    /**
     * Get chronic diseases catalog
     */
    @GetMapping("/catalog/chronic-diseases")
    public ResponseEntity<List<ChronicDiseaseDTO>> getChronicDiseases() {
        List<ChronicDiseaseDTO> diseases = patientService.getChronicDiseases();
        return ResponseEntity.ok(diseases);
    }

    /**
     * Get affected zones catalog
     */
    @GetMapping("/catalog/affected-zones")
    public ResponseEntity<List<AffectedZoneDTO>> getAffectedZones() {
        List<AffectedZoneDTO> zones = patientService.getAffectedZones();
        return ResponseEntity.ok(zones);
    }

    /**
     * Get lesion types catalog
     */
    @GetMapping("/catalog/lesion-types")
    public ResponseEntity<List<LesionTypeDTO>> getLesionTypes() {
        List<LesionTypeDTO> types = patientService.getLesionTypes();
        return ResponseEntity.ok(types);
    }

    /**
     * Get physiotherapist routines
     */
    @GetMapping("/routines")
    public ResponseEntity<RoutinesResponse> getPhysioRoutines(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        Long userId = extractUserIdFromToken(authorizationHeader);
        RoutinesResponse response = patientService.getPhysioRoutines(userId);

        return ResponseEntity.ok(response);
    }

    /**
     * Create or update patient profile
     */
    @PostMapping("/profile")
    public ResponseEntity<PatientProfileResponse> createOrUpdateProfile(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody PatientProfileRequest request) {

        Long userId = extractUserIdFromToken(authorizationHeader);
        PatientProfileResponse response = patientService.createOrUpdatePatientProfile(request, userId);

        return ResponseEntity.ok(response);
    }

    /**
     * Delete routine assignment
     */
    @PostMapping("/routine/delete")
    public ResponseEntity<ApiResponse> deleteRoutineAssignment(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @Valid @RequestBody DeleteRoutineRequest request) {

        Long userId = extractUserIdFromToken(authorizationHeader);
        ApiResponse response = patientService.deleteRoutineAssignment(
                request.getPatientProfileId(),
                request.getRoutineSessionId(),
                userId);

        return ResponseEntity.ok(response);
    }

    /**
     * Get a specific patient profile by ID
     */
    @GetMapping("/profile/{id}")
    public ResponseEntity<PatientProfileResponse> getPatientProfile(
            @PathVariable Long id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        Long userId = extractUserIdFromToken(authorizationHeader);
        PatientProfileResponse response = patientService.getPatientProfile(id, userId);

        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get all patient profiles for the authenticated user
     */
    @GetMapping("/profiles")
    public ResponseEntity<List<PatientProfileDTO>> getAllPatientProfiles(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        Long userId = extractUserIdFromToken(authorizationHeader);
        List<PatientProfileDTO> profiles = patientService.getAllPatientProfiles(userId);

        return ResponseEntity.ok(profiles);
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
