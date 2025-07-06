package com.ms_fisio.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.patient.domain.model.ChronicDiseaseModel;

/**
 * Repository interface for ChronicDisease entity
 */
@Repository
public interface ChronicDiseaseRepository extends JpaRepository<ChronicDiseaseModel, Long> {
}
