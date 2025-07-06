package com.ms_fisio.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.patient.domain.model.PatientRoutineModel;

/**
 * Repository interface for PatientRoutine entity
 */
@Repository
public interface PatientRoutineRepository extends JpaRepository<PatientRoutineModel, PatientRoutineModel.PatientRoutineId> {
}
