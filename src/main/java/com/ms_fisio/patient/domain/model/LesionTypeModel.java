package com.ms_fisio.patient.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Lesion type entity for injury classifications
 */
@Entity
@Table(name = "lesion_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LesionTypeModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesion_type_id")
    private Long lesionTypeId;
    
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    // Relationships
    @OneToMany(mappedBy = "lesionType", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PatientLesionModel> patientLesions;
}
