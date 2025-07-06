package com.ms_fisio.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for Google user information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserInfo {
    
    private String googleId;
    private String email;
    private String name;
    private String picture;
    private Boolean emailVerified;
}
