package com.ms_fisio.patient.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ms_fisio.session.domain.model.RoutineSessionModel;

/**
 * Junction table between patients and routine sessions
 */
@Entity
@Table(name = "patient_routines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientRoutineModel {
    
    @EmbeddedId
    private PatientRoutineId patientRoutineId;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patientProfileId")
    @JoinColumn(name = "profile_id", nullable = false)
    private PatientProfileModel patientProfile;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("routineSessionId")
    @JoinColumn(name = "session_id", nullable = false)
    private RoutineSessionModel routineSession;
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientRoutineId {
        @Column(name = "profile_id")
        private Long patientProfileId;
        
        @Column(name = "session_id")
        private Long routineSessionId;
    }
}
