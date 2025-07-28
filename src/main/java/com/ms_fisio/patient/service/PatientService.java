package com.ms_fisio.patient.service;

import com.ms_fisio.patient.domain.dto.*;
import com.ms_fisio.patient.domain.model.*;
import com.ms_fisio.patient.repository.*;
import com.ms_fisio.patient.exception.UnauthorizedAccessException;
import com.ms_fisio.routine.dto.RoutinesResponse;
import com.ms_fisio.routine.service.RoutineService;
import com.ms_fisio.routine.repository.RoutineRepository;
import com.ms_fisio.routine.domain.model.RoutineModel;
import com.ms_fisio.session.domain.model.RoutineSessionModel;
import com.ms_fisio.session.repository.RoutineSessionRepository;
import com.ms_fisio.user.domain.model.UserModel;
import com.ms_fisio.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Service for patient operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {
    
    private final ChronicDiseaseRepository chronicDiseaseRepository;
    private final AffectedZoneRepository affectedZoneRepository;
    private final LesionTypeRepository lesionTypeRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final PatientDiseaseRepository patientDiseaseRepository;
    private final PatientZoneRepository patientZoneRepository;
    private final PatientLesionRepository patientLesionRepository;
    private final PatientRoutineRepository patientRoutineRepository;
    private final UserRepository userRepository;
    private final RoutineService routineService;
    private final RoutineRepository routineRepository;
    private final RoutineSessionRepository routineSessionRepository;
    
    /**
     * Get all chronic diseases catalog for a user
     */
    public List<ChronicDiseaseDTO> getChronicDiseases(Long userId) {
        log.info("Fetching all chronic diseases for user: {}", userId);
        return chronicDiseaseRepository.findByUserId(userId).stream()
                .map(disease -> new ChronicDiseaseDTO(disease.getChronicDiseaseId(), disease.getName()))
                .toList();
    }

    /**
     * Get all affected zones catalog for a user
     */
    public List<AffectedZoneDTO> getAffectedZones(Long userId) {
        log.info("Fetching all affected zones for user: {}", userId);
        return affectedZoneRepository.findByUserId(userId).stream()
                .map(zone -> new AffectedZoneDTO(zone.getAffectedZoneId(), zone.getName()))
                .toList();
    }

    /**
     * Get all lesion types catalog for a user
     */
    public List<LesionTypeDTO> getLesionTypes(Long userId) {
        log.info("Fetching all lesion types for user: {}", userId);
        return lesionTypeRepository.findByUserId(userId).stream()
                .map(type -> new LesionTypeDTO(type.getLesionTypeId(), type.getName()))
                .toList();
    }
    
    /**
     * Get physiotherapist routines for a user
     */
    public RoutinesResponse getPhysioRoutines(Long userId) {
        log.info("Fetching physiotherapist routines for user: {}", userId);
        return routineService.getAllRoutines(userId);
    }
    
    /**
     * Create or update patient profile
     */
    @Transactional
    public PatientProfileResponse createOrUpdatePatientProfile(PatientProfileRequest request, Long userId) {
        try {
            log.info("Creating/updating patient profile for user: {}", userId);
            
            // Get user
            UserModel user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            
            PatientProfileModel patientProfile;
            boolean isNewProfile = false;
            
            if (request.getId() != null) {
                // Update existing profile
                patientProfile = patientProfileRepository.findById(request.getId())
                        .orElseThrow(() -> new RuntimeException("Patient profile not found"));
                log.info("Updating existing patient profile with ID: {}", request.getId());
            } else {
                // Create new profile
                patientProfile = new PatientProfileModel();
                patientProfile.setUser(user);
                isNewProfile = true;
                log.info("Creating new patient profile");
            }
            
            // Update user basic information
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            userRepository.save(user);
            
            // Update patient profile information
            patientProfile.setIdNumber(request.getIdNumber());
            patientProfile.setPhone(request.getPhone());
            patientProfile.setAge(request.getAge());
            patientProfile.setSex(request.getSex());
            patientProfile.setPriorSurgeries(request.getPriorSurgeries());
            patientProfile.setPainDate(request.getPainStartDate());
            patientProfile.setPainLevel(request.getPainLevel());
            patientProfile.setDiagnosis(request.getMedicalDiagnosis());
            
            patientProfile = patientProfileRepository.save(patientProfile);
            
            // Update chronic diseases associations
            updatePatientDiseases(patientProfile, request.getChronicDiseaseIds());
            
            // Update affected zones associations
            updatePatientZones(patientProfile, request.getAffectedZoneIds());
            
            // Update lesion types associations
            updatePatientLesions(patientProfile, request.getLesionTypeIds());
            
            // Create session for new profiles only
            Long sessionId = null;
            String accessCode = null;
            
            if (isNewProfile) {
                RoutineSessionModel session = createPatientSession(request.getRoutineId());
                sessionId = session.getRoutineSessionId();
                accessCode = session.getAccessCode();
                log.info("Created session {} with access code {} for new patient profile", sessionId, accessCode);
            }
            
            return PatientProfileResponse.success(
                "Patient profile saved successfully", 
                patientProfile.getPatientProfileId(),
                sessionId,
                accessCode
            );
            
        } catch (Exception e) {
            log.error("Error creating/updating patient profile: {}", e.getMessage());
            return PatientProfileResponse.error("Error saving patient profile: " + e.getMessage());
        }
    }
    
    /**
     * Delete a routine assignment for a patient
     */
    @Transactional
    public ApiResponse deleteRoutineAssignment(Long patientProfileId, Long routineSessionId, Long userId) {
        try {
            log.info("Deleting routine assignment for patient: {} and session: {} by user: {}", 
                    patientProfileId, routineSessionId, userId);
            
            PatientRoutineModel.PatientRoutineId routineId = 
                    new PatientRoutineModel.PatientRoutineId(patientProfileId, routineSessionId);
            
            PatientRoutineModel patientRoutine = patientRoutineRepository.findById(routineId)
                    .orElseThrow(() -> new RuntimeException("Routine assignment not found"));
            
            // Verify that the routine belongs to the user
            if (!patientRoutine.getPatientProfile().getUser().getUserId().equals(userId)) {
                throw new UnauthorizedAccessException("Unauthorized to delete this routine assignment");
            }
            
            patientRoutineRepository.delete(patientRoutine);
            
            return new ApiResponse(true, "Routine assignment deleted successfully");
            
        } catch (Exception e) {
            log.error("Error deleting routine assignment: {}", e.getMessage());
            return new ApiResponse(false, "Error deleting routine assignment: " + e.getMessage());
        }
    }
    
    /**
     * Update patient chronic diseases associations
     */
    private void updatePatientDiseases(PatientProfileModel patientProfile, List<Long> diseaseIds) {
        if (diseaseIds == null) return;
        
        // Remove existing associations
        patientDiseaseRepository.deleteByPatientProfile(patientProfile);
        
        // Add new associations
        for (Long diseaseId : diseaseIds) {
            ChronicDiseaseModel disease = chronicDiseaseRepository.findById(diseaseId)
                    .orElseThrow(() -> new RuntimeException("Chronic disease not found: " + diseaseId));
            
            PatientDiseaseModel patientDisease = new PatientDiseaseModel();
            
            // Create and set the composite key
            PatientDiseaseModel.PatientDiseaseId compositeKey = new PatientDiseaseModel.PatientDiseaseId();
            compositeKey.setPatientProfileId(patientProfile.getPatientProfileId());
            compositeKey.setChronicDiseaseId(disease.getChronicDiseaseId());
            patientDisease.setPatientDiseaseId(compositeKey);
            
            // Set the relationships
            patientDisease.setPatientProfile(patientProfile);
            patientDisease.setChronicDisease(disease);
            patientDiseaseRepository.save(patientDisease);
        }
    }
    
    /**
     * Update patient affected zones associations
     */
    private void updatePatientZones(PatientProfileModel patientProfile, List<Long> zoneIds) {
        if (zoneIds == null) return;
        
        // Remove existing associations
        patientZoneRepository.deleteByPatientProfile(patientProfile);
        
        // Add new associations
        for (Long zoneId : zoneIds) {
            AffectedZoneModel zone = affectedZoneRepository.findById(zoneId)
                    .orElseThrow(() -> new RuntimeException("Affected zone not found: " + zoneId));
            
            PatientZoneModel patientZone = new PatientZoneModel();
            
            // Create and set the composite key
            PatientZoneModel.PatientZoneId compositeKey = new PatientZoneModel.PatientZoneId();
            compositeKey.setPatientProfileId(patientProfile.getPatientProfileId());
            compositeKey.setAffectedZoneId(zone.getAffectedZoneId());
            patientZone.setPatientZoneId(compositeKey);
            
            // Set the relationships
            patientZone.setPatientProfile(patientProfile);
            patientZone.setAffectedZone(zone);
            patientZoneRepository.save(patientZone);
        }
    }
    
    /**
     * Update patient lesion types associations
     */
    private void updatePatientLesions(PatientProfileModel patientProfile, List<Long> lesionIds) {
        if (lesionIds == null) return;
        
        // Remove existing associations
        patientLesionRepository.deleteByPatientProfile(patientProfile);
        
        // Add new associations
        for (Long lesionId : lesionIds) {
            LesionTypeModel lesionType = lesionTypeRepository.findById(lesionId)
                    .orElseThrow(() -> new RuntimeException("Lesion type not found: " + lesionId));
            
            PatientLesionModel patientLesion = new PatientLesionModel();
            
            // Create and set the composite key
            PatientLesionModel.PatientLesionId compositeKey = new PatientLesionModel.PatientLesionId();
            compositeKey.setPatientProfileId(patientProfile.getPatientProfileId());
            compositeKey.setLesionTypeId(lesionType.getLesionTypeId());
            patientLesion.setPatientLesionId(compositeKey);
            
            // Set the relationships
            patientLesion.setPatientProfile(patientProfile);
            patientLesion.setLesionType(lesionType);
            patientLesionRepository.save(patientLesion);
        }
    }
    
    /**
     * Create a routine session for a patient profile
     */
    private RoutineSessionModel createPatientSession(Long routineId) {
        RoutineSessionModel session = new RoutineSessionModel();
        
        // Set the routine for the session
        RoutineModel routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new IllegalArgumentException("Routine not found with ID: " + routineId));
        session.setRoutine(routine);
        
        // Generate unique access code
        String accessCode = generateSessionAccessCode();
        session.setAccessCode(accessCode);
        
        // Set session timestamps
        session.setStartDatetime(LocalDateTime.now());
        // End datetime will be set when session is actually started
        
        session = routineSessionRepository.save(session);
        log.info("Created session with ID: {} and access code: {} for routine: {}", 
                 session.getRoutineSessionId(), accessCode, routine.getName());
        
        return session;
    }
    
    /**
     * Generate a unique session access code
     */
    private String generateSessionAccessCode() {
        return "PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    /**
     * Get a specific patient profile by ID
     */
    public PatientProfileResponse getPatientProfile(Long patientProfileId, Long userId) {
        try {
            log.info("Fetching patient profile with ID: {} for user: {}", patientProfileId, userId);
            
            PatientProfileModel patientProfile = patientProfileRepository.findById(patientProfileId)
                    .orElseThrow(() -> new IllegalArgumentException("Patient profile not found with ID: " + patientProfileId));
            
            // Check if user has access to this profile
            if (!patientProfile.getUser().getUserId().equals(userId)) {
                throw new UnauthorizedAccessException("User does not have access to this patient profile");
            }
            
            // Convert to DTO
            PatientProfileDTO profileDTO = convertToPatientProfileDTO(patientProfile);
            
            return PatientProfileResponse.success(
                "Patient profile retrieved successfully", 
                profileDTO.getPatientProfileId(),
                null, // No session creation for retrieval
                null, // No access code for retrieval
                profileDTO
            );
            
        } catch (UnauthorizedAccessException e) {
            log.error("Unauthorized access to patient profile: {}", e.getMessage());
            return PatientProfileResponse.error("Unauthorized access to patient profile");
        } catch (Exception e) {
            log.error("Error fetching patient profile: {}", e.getMessage());
            return PatientProfileResponse.error("Error retrieving patient profile: " + e.getMessage());
        }
    }
    
    /**
     * Get all patient profiles for a user
     */
    public List<PatientProfileDTO> getAllPatientProfiles(Long userId) {
        try {
            log.info("Fetching all patient profiles for user: {}", userId);
            
            List<PatientProfileModel> profiles = patientProfileRepository.findByUserId(userId);
            
            return profiles.stream()
                    .map(this::convertToPatientProfileDTO)
                    .toList();
                    
        } catch (Exception e) {
            log.error("Error fetching patient profiles: {}", e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Convert PatientProfileModel to PatientProfileDTO
     */
    private PatientProfileDTO convertToPatientProfileDTO(PatientProfileModel profile) {
        PatientProfileDTO dto = new PatientProfileDTO();
        dto.setPatientProfileId(profile.getPatientProfileId());
        dto.setFullName(""); // PatientProfileModel doesn't have fullName field
        dto.setIdNumber(profile.getIdNumber());
        dto.setPhone(profile.getPhone());
        dto.setAge(profile.getAge());
        dto.setSex(profile.getSex());
        dto.setEmail(""); // PatientProfileModel doesn't have email field
        dto.setPriorSurgeries(profile.getPriorSurgeries());
        dto.setPainStartDate(profile.getPainDate()); // Using painDate field
        dto.setPainLevel(profile.getPainLevel());
        dto.setMedicalDiagnosis(profile.getDiagnosis()); // Using diagnosis field
        dto.setUserId(profile.getUser().getUserId());
        dto.setCreatedAt(null); // PatientProfileModel doesn't have createdAt field
        dto.setUpdatedAt(null); // PatientProfileModel doesn't have updatedAt field
        
        // Convert chronic diseases
        List<ChronicDiseaseDTO> diseases = profile.getPatientDiseases().stream()
                .map(pd -> new ChronicDiseaseDTO(
                    pd.getChronicDisease().getChronicDiseaseId(),
                    pd.getChronicDisease().getName()))
                .toList();
        dto.setChronicDiseases(diseases);
        
        // Convert affected zones
        List<AffectedZoneDTO> zones = profile.getPatientZones().stream()
                .map(pz -> new AffectedZoneDTO(
                    pz.getAffectedZone().getAffectedZoneId(),
                    pz.getAffectedZone().getName()))
                .toList();
        dto.setAffectedZones(zones);
        
        // Convert lesion types
        List<LesionTypeDTO> lesions = profile.getPatientLesions().stream()
                .map(pl -> new LesionTypeDTO(
                    pl.getLesionType().getLesionTypeId(),
                    pl.getLesionType().getName()))
                .toList();
        dto.setLesionTypes(lesions);
        
        return dto;
    }
}
