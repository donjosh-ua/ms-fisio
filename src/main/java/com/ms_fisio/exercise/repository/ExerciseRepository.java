package com.ms_fisio.exercise.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.ms_fisio.exercise.domain.model.ExerciseModel;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Exercise entity
 */
@Repository
public interface ExerciseRepository extends JpaRepository<ExerciseModel, Long> {

    @NonNull
    @EntityGraph(attributePaths = { "exerciseMoments", "objectiveArea" })
    List<ExerciseModel> findAll();

    @NonNull
    @EntityGraph(attributePaths = { "exerciseMoments", "objectiveArea" })
    Optional<ExerciseModel> findById(@NonNull Long id);
}
