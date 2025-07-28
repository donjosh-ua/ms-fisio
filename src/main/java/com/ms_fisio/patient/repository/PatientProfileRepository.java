package com.ms_fisio.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ms_fisio.patient.domain.model.PatientProfileModel;
import java.util.List;

/**
 * Repository interface for PatientProfile entity
 */
@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfileModel, Long> {
    @Query("SELECT pp FROM PatientProfileModel pp WHERE pp.user.userId = :userId")
    List<PatientProfileModel> findByUserId(Long userId);
}
