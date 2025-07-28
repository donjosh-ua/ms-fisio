package com.ms_fisio.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ms_fisio.patient.domain.model.LesionTypeModel;

/**
 * Repository interface for LesionType entity
 */
@Repository
public interface LesionTypeRepository extends JpaRepository<LesionTypeModel, Long> {
    @Query("SELECT lt FROM LesionTypeModel lt JOIN lt.patientLesions pl JOIN pl.patientProfile pp WHERE pp.user.userId = :userId")
    java.util.List<LesionTypeModel> findByUserId(Long userId);
}
