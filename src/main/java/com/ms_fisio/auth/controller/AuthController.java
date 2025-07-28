package com.ms_fisio.auth.controller;

import com.ms_fisio.auth.dto.AuthResponse;
import com.ms_fisio.auth.dto.GoogleLoginRequest;
import com.ms_fisio.auth.dto.LoginRequest;
import com.ms_fisio.auth.dto.RegisterRequest;
import com.ms_fisio.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controller for authentication endpoints
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthController {
    
    private final AuthService authService;
    
    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        log.info("Health check called");
        return ResponseEntity.ok("Auth service is running - Development Mode");
    }
    
    /**
     * Simple test endpoint
     */
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        log.info("Test endpoint called");
        return ResponseEntity.ok("Test endpoint working");
    }
    
    /**
     * Login with email and password
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());
        
        try {
            AuthResponse response = authService.authenticateUser(request.getEmail(), request.getPassword());
            log.info("User logged in successfully: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Login failed for email: {}", request.getEmail(), e);
            throw e;
        }
    }
    
    /**
     * Login with Google OAuth token
     */
    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleLogin(@Valid @RequestBody GoogleLoginRequest request) {
        log.info("Google login attempt");
        
        AuthResponse response = authService.authenticateGoogleUser(request.getGoogleToken());
        
        log.info("User logged in with Google successfully");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Refresh access token using refresh token
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        log.info("Token refresh attempt");
        
        if (authorizationHeader == null || authorizationHeader.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        
        AuthResponse response = authService.refreshToken(authorizationHeader);
        
        log.info("Token refreshed successfully");
        return ResponseEntity.ok(response);
    }
    
    /**
     * Logout user
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        log.info("Logout attempt");
        
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            authService.logout(authorizationHeader);
        }
        
        log.info("User logged out successfully");
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Register a new user
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register attempt for email: {}", request.getEmail());
        try {
            AuthResponse response = authService.registerUser(request);
            log.info("User registered successfully: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Registration failed for email: {}", request.getEmail(), e);
            throw e;
        }
    }
}