package com.ms_fisio.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ms_fisio.patient.domain.model.AffectedZoneModel;

import java.util.List;

/**
 * Repository interface for AffectedZone entity
 */
@Repository
public interface AffectedZoneRepository extends JpaRepository<AffectedZoneModel, Long> {
    @Query("SELECT az FROM AffectedZoneModel az JOIN az.patientZones pz JOIN pz.patientProfile pp WHERE pp.user.userId = :userId")
    List<AffectedZoneModel> findByUserId(Long userId);
}
