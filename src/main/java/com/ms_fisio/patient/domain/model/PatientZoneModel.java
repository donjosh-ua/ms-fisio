package com.ms_fisio.patient.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Junction table between patients and affected zones
 */
@Entity
@Table(name = "patient_zones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientZoneModel {
    
    @EmbeddedId
    private PatientZoneId patientZoneId;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("patientProfileId")
    @JoinColumn(name = "profile_id", nullable = false)
    private PatientProfileModel patientProfile;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("affectedZoneId")
    @JoinColumn(name = "zone_id", nullable = false)
    private AffectedZoneModel affectedZone;
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PatientZoneId {
        @Column(name = "profile_id")
        private Long patientProfileId;
        
        @Column(name = "zone_id")
        private Long affectedZoneId;
    }
}
