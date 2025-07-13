package com.ms_fisio.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ms_fisio.patient.domain.model.PatientDiseaseModel;
import com.ms_fisio.patient.domain.model.PatientProfileModel;

/**
 * Repository interface for PatientDisease entity
 */
@Repository
public interface PatientDiseaseRepository extends JpaRepository<PatientDiseaseModel, PatientDiseaseModel.PatientDiseaseId> {
    
    @Modifying
    @Query("DELETE FROM PatientDiseaseModel pd WHERE pd.patientProfile = :patientProfile")
    void deleteByPatientProfile(@Param("patientProfile") PatientProfileModel patientProfile);
}
