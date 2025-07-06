package com.ms_fisio.routine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.routine.domain.model.RoutineDayModel;

/**
 * Repository interface for RoutineDay entity
 */
@Repository
public interface RoutineDayRepository extends JpaRepository<RoutineDayModel, RoutineDayModel.RoutineDayId> {
}
