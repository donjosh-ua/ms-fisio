package com.ms_fisio.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Google OAuth login request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleLoginRequest {
    
    @NotBlank(message = "Google token is required")
    private String googleToken;
}
