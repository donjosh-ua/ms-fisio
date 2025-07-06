package com.ms_fisio.exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.exercise.domain.model.ExerciseMomentModel;

/**
 * Repository interface for ExerciseMoment entity
 */
@Repository
public interface ExerciseMomentRepository extends JpaRepository<ExerciseMomentModel, Long> {
}
