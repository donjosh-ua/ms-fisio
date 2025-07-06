package com.ms_fisio.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.patient.domain.model.PatientZoneModel;

/**
 * Repository interface for PatientZone entity
 */
@Repository
public interface PatientZoneRepository extends JpaRepository<PatientZoneModel, PatientZoneModel.PatientZoneId> {
}
