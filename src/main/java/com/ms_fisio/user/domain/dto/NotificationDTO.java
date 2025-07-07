package com.ms_fisio.user.domain.dto;

import java.time.LocalDateTime;


public class NotificationDTO {
    public Long notificationId;
    public String type;
    public String sender_name;
    public String message   ;
    public LocalDateTime sentAt;    
}