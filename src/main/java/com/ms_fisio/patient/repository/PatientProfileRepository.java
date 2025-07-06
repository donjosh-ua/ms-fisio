package com.ms_fisio.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.patient.domain.model.PatientProfileModel;

/**
 * Repository interface for PatientProfile entity
 */
@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfileModel, Long> {
}
