package com.ms_fisio.patient.service;

import com.ms_fisio.patient.domain.dto.*;
import com.ms_fisio.patient.domain.dto.ChronicDiseaseDTO;
import com.ms_fisio.patient.domain.dto.AffectedZoneDTO;
import com.ms_fisio.patient.domain.dto.LesionTypeDTO;
import com.ms_fisio.patient.domain.model.*;
import com.ms_fisio.patient.repository.*;
import com.ms_fisio.patient.exception.UnauthorizedAccessException;
import com.ms_fisio.routine.dto.RoutinesResponse;
import com.ms_fisio.routine.service.RoutineService;
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
    private final RoutineSessionRepository routineSessionRepository;
    
    /**
     * Get all chronic diseases catalog
     */
    public List<ChronicDiseaseDTO> getChronicDiseases() {
        log.info("Fetching all chronic diseases");
        return chronicDiseaseRepository.findAll().stream()
                .map(disease -> new ChronicDiseaseDTO(disease.getChronicDiseaseId(), disease.getName()))
                .toList();
    }
    
    /**
     * Get all affected zones catalog
     */
    public List<AffectedZoneDTO> getAffectedZones() {
        log.info("Fetching all affected zones");
        return affectedZoneRepository.findAll().stream()
                .map(zone -> new AffectedZoneDTO(zone.getAffectedZoneId(), zone.getName()))
                .toList();
    }
    
    /**
     * Get all lesion types catalog
     */
    public List<LesionTypeDTO> getLesionTypes() {
        log.info("Fetching all lesion types");
        return lesionTypeRepository.findAll().stream()
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
                RoutineSessionModel session = createPatientSession();
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
            patientLesion.setPatientProfile(patientProfile);
            patientLesion.setLesionType(lesionType);
            patientLesionRepository.save(patientLesion);
        }
    }
    
    /**
     * Create a routine session for a patient profile
     */
    private RoutineSessionModel createPatientSession() {
        RoutineSessionModel session = new RoutineSessionModel();
        
        // Generate unique access code
        String accessCode = generateSessionAccessCode();
        session.setAccessCode(accessCode);
        
        // Set session timestamps
        session.setStartDatetime(LocalDateTime.now());
        // Note: routine field can be null for now, as this is for patient profile creation
        // End datetime will be set when session is actually started
        
        session = routineSessionRepository.save(session);
        log.info("Created session with ID: {} and access code: {}", session.getRoutineSessionId(), accessCode);
        
        return session;
    }
    
    /**
     * Generate a unique session access code
     */
    private String generateSessionAccessCode() {
        return "PAT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
