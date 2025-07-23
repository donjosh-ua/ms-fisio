package com.ms_fisio.subscription.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.subscription.domain.model.SubscriptionModel;

/**
 * Repository interface for Subscription entity
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<SubscriptionModel, Long> {
boolean existsByUserUserId(Long userId);
}
