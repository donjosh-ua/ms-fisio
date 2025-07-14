package com.ms_fisio.routine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.routine.domain.model.RoutineModel;

import java.util.List;

/**
 * Repository interface for Routine entity
 */
@Repository
public interface RoutineRepository extends JpaRepository<RoutineModel, Long> {
    List<RoutineModel> findByCreatedByUser_UserId(Long userId);
}
