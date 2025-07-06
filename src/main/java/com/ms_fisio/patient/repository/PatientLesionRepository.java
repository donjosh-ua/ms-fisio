package com.ms_fisio.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.patient.domain.model.PatientLesionModel;

/**
 * Repository interface for PatientLesion entity
 */
@Repository
public interface PatientLesionRepository extends JpaRepository<PatientLesionModel, PatientLesionModel.PatientLesionId> {
}
