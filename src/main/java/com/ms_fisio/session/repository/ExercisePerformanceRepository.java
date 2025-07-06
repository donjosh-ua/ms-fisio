package com.ms_fisio.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.session.domain.model.ExercisePerformanceModel;

/**
 * Repository interface for ExercisePerformance entity
 */
@Repository
public interface ExercisePerformanceRepository extends JpaRepository<ExercisePerformanceModel, Long> {
}
