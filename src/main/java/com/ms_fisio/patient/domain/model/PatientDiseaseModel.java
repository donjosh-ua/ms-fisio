package com.ms_fisio.patient.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Junction table between patients and chronic diseases
 */
@Entity
@Table(name = "patient_diseases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientDiseaseModel {
    
    @EmbeddedId
    private PatientDiseaseId patientDiseaseId;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patientProfileId")
    @JoinColumn(name = "profile_id", nullable = false)
    private PatientProfileModel patientProfile;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("chronicDiseaseId")
    @JoinColumn(name = "disease_id", nullable = false)
    private ChronicDiseaseModel chronicDisease;
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientDiseaseId {
        @Column(name = "profile_id")
        private Long patientProfileId;
        
        @Column(name = "disease_id")
        private Long chronicDiseaseId;
    }
}
