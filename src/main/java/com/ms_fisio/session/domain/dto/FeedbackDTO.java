package com.ms_fisio.session.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for feedback operations
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackDTO {
    private Long feedbackId;
    
    @NotBlank(message = "Feedback content is required")
    private String feedback;
    
    private Integer calification; // Rating/score for the session
    
    private Long sessionId; // For response purposes
}