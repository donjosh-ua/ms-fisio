package com.ms_fisio.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.session.domain.model.RoutineSessionModel;

/**
 * Repository interface for RoutineSession entity
 */
@Repository
public interface RoutineSessionRepository extends JpaRepository<RoutineSessionModel, Long> {
}
