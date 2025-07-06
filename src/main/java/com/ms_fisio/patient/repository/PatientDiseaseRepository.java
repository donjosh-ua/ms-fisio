package com.ms_fisio.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.patient.domain.model.PatientDiseaseModel;

/**
 * Repository interface for PatientDisease entity
 */
@Repository
public interface PatientDiseaseRepository extends JpaRepository<PatientDiseaseModel, PatientDiseaseModel.PatientDiseaseId> {
}
