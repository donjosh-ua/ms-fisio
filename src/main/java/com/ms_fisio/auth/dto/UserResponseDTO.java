package com.ms_fisio.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for user information in authentication responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    
    private Long userId;
    private String fullName;
    private String username;
    private String email;
    private String profilePhoto;
    private Boolean emailVerified;
}
