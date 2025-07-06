package com.ms_fisio.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.subscription.domain.model.PlanTypeModel;

/**
 * Repository interface for PlanType entity
 */
@Repository
public interface PlanTypeRepository extends JpaRepository<PlanTypeModel, Long> {
}
