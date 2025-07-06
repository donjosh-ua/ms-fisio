package com.ms_fisio.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.session.domain.model.FeedbackModel;

/**
 * Repository interface for Feedback entity
 */
@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackModel, Long> {
}
