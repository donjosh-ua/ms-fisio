package com.ms_fisio.dashboard.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for notification information in dashboard
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {
    
    private String originName;
    private String sentAt;
    private String content;
}
