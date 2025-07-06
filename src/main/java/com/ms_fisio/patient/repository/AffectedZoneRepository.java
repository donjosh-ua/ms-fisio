package com.ms_fisio.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.patient.domain.model.AffectedZoneModel;

/**
 * Repository interface for AffectedZone entity
 */
@Repository
public interface AffectedZoneRepository extends JpaRepository<AffectedZoneModel, Long> {
}
