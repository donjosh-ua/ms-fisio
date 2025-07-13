package com.ms_fisio.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ms_fisio.patient.domain.model.PatientLesionModel;
import com.ms_fisio.patient.domain.model.PatientProfileModel;

/**
 * Repository interface for PatientLesion entity
 */
@Repository
public interface PatientLesionRepository extends JpaRepository<PatientLesionModel, PatientLesionModel.PatientLesionId> {
    
    @Modifying
    @Query("DELETE FROM PatientLesionModel pl WHERE pl.patientProfile = :patientProfile")
    void deleteByPatientProfile(@Param("patientProfile") PatientProfileModel patientProfile);
}
