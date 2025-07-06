package com.ms_fisio.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.patient.domain.model.LesionTypeModel;

/**
 * Repository interface for LesionType entity
 */
@Repository
public interface LesionTypeRepository extends JpaRepository<LesionTypeModel, Long> {
}
