package com.ms_fisio.user.service;

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
}