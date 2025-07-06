package com.ms_fisio.exercise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.exercise.domain.model.ObjectiveAreaModel;

/**
 * Repository interface for ObjectiveArea entity
 */
@Repository
public interface ObjectiveAreaRepository extends JpaRepository<ObjectiveAreaModel, Long> {
}
