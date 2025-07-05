package com.ms_fisio.patient.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Chronic disease entity for medical conditions
 */
@Entity
@Table(name = "chronic_diseases")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChronicDiseaseModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chronic_disease_id")
    private Long chronicDiseaseId;
    
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    // Relationships
    @OneToMany(mappedBy = "chronicDisease", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PatientDiseaseModel> patientDiseases;
}
