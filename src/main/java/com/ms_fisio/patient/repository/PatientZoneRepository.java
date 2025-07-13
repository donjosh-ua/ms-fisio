package com.ms_fisio.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ms_fisio.patient.domain.model.PatientZoneModel;
import com.ms_fisio.patient.domain.model.PatientProfileModel;

/**
 * Repository interface for PatientZone entity
 */
@Repository
public interface PatientZoneRepository extends JpaRepository<PatientZoneModel, PatientZoneModel.PatientZoneId> {
    
    @Modifying
    @Query("DELETE FROM PatientZoneModel pz WHERE pz.patientProfile = :patientProfile")
    void deleteByPatientProfile(@Param("patientProfile") PatientProfileModel patientProfile);
}
