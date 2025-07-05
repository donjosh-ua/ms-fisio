package com.ms_fisio.patient.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Affected zone entity for body areas
 */
@Entity
@Table(name = "affected_zones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffectedZoneModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "affected_zone_id")
    private Long affectedZoneId;
    
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    // Relationships
    @OneToMany(mappedBy = "affectedZone", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PatientZoneModel> patientZones;
}
