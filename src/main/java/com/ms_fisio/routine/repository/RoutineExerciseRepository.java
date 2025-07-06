package com.ms_fisio.routine.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.routine.domain.model.RoutineExerciseModel;

/**
 * Repository interface for RoutineExercise entity
 */
@Repository
public interface RoutineExerciseRepository extends JpaRepository<RoutineExerciseModel, RoutineExerciseModel.RoutineExerciseId> {
}
