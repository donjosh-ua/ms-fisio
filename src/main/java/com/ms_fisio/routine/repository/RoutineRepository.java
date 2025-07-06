package com.ms_fisio.routine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.routine.domain.model.RoutineModel;

/**
 * Repository interface for Routine entity
 */
@Repository
public interface RoutineRepository extends JpaRepository<RoutineModel, Long> {
}
