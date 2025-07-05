package com.ms_fisio.patient.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Junction table between patients and lesion types
 */
@Entity
@Table(name = "patient_lesions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientLesionModel {
    
    @EmbeddedId
    private PatientLesionId patientLesionId;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patientProfileId")
    @JoinColumn(name = "profile_id", nullable = false)
    private PatientProfileModel patientProfile;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("lesionTypeId")
    @JoinColumn(name = "lesion_id", nullable = false)
    private LesionTypeModel lesionType;
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientLesionId {
        @Column(name = "profile_id")
        private Long patientProfileId;
        
        @Column(name = "lesion_id")
        private Long lesionTypeId;
    }
}
