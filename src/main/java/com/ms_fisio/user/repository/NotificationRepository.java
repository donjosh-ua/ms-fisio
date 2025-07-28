package com.ms_fisio.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.user.domain.model.NotificationModel;

/**
 * Repository interface for Notification entity
 */
@Repository
public interface NotificationRepository extends JpaRepository<NotificationModel, Long> {
    List<NotificationModel> findByRecipientUserId(Long recipientId);   
}
