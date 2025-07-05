package com.ms_fisio.patient.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import com.ms_fisio.user.domain.model.UserModel;

/**
 * Patient profile entity for extended medical information
 */
@Entity
@Table(name = "patient_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientProfileModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_profile_id")
    private Long patientProfileId;
    
    @Column(name = "id_number", length = 50, unique = true)
    private String idNumber;
    
    @Column(name = "age")
    private Integer age;
    
    @Column(name = "sex", length = 10)
    private String sex;
    
    @Column(name = "phone", length = 20)
    private String phone;
    
    @Column(name = "prior_surgeries", columnDefinition = "TEXT")
    private String priorSurgeries;
    
    @Column(name = "pain_date")
    private LocalDate painDate;
    
    @Column(name = "pain_level")
    private Integer painLevel;
    
    @Column(name = "diagnosis", columnDefinition = "TEXT")
    private String diagnosis;
    
    // Relationships
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserModel user;
    
    @OneToMany(mappedBy = "patientProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PatientDiseaseModel> patientDiseases;
    
    @OneToMany(mappedBy = "patientProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PatientZoneModel> patientZones;
    
    @OneToMany(mappedBy = "patientProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PatientLesionModel> patientLesions;
    
    @OneToMany(mappedBy = "patientProfile", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PatientRoutineModel> patientRoutines;
}
