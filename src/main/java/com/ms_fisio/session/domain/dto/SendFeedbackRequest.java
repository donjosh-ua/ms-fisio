package com.ms_fisio.session.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for sending feedback with access code
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendFeedbackRequest {
    
    @NotBlank(message = "Access code is required")
    private String accessCode;
    
    @NotBlank(message = "Feedback content is required")
    private String feedback;
    
    private Integer calification; // Rating/score for the session (e.g., 1-5 or 1-10)
}
