package com.ms_fisio.user.service;

import com.ms_fisio.user.domain.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {

    /**
     * Marks a notification as read.
     *
     * @param notificationId the ID of the notification to mark as read
     */
    void markAsRead(Long notificationId);

    /**
     * Deletes a notification.
     *
     * @param notificationId the ID of the notification to delete
     */
    void deleteNotification(Long notificationId);

    /**
     * Retrieves all notifications for a given recipient.
     *
     * @param recipientId the ID of the recipient
     * @return a list of notifications
     */
    List<NotificationDTO> getNotificationsByRecipientId(Long recipientId);
}
