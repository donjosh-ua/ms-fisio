package com.ms_fisio.auth.service;

import com.ms_fisio.auth.dto.AuthResponse;
import com.ms_fisio.auth.dto.GoogleUserInfo;
import com.ms_fisio.auth.dto.RegisterRequest;
import com.ms_fisio.auth.dto.UserResponseDTO;
import com.ms_fisio.auth.exception.AuthenticationException;
import com.ms_fisio.auth.exception.InvalidTokenException;
import com.ms_fisio.user.domain.model.UserModel;
import com.ms_fisio.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for authentication operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final GoogleOAuthService googleOAuthService;
    
    @Value("${app.jwt.access-token-expiration}")
    private Long accessTokenExpiration;
    
    /**
     * Authenticate user with email and password
     */
    public AuthResponse authenticateUser(String email, String password) {
        Optional<UserModel> userOptional = userRepository.findByEmail(email);
        
        if (userOptional.isEmpty()) {
            throw new AuthenticationException("Invalid email or password");
        }
        
        UserModel user = userOptional.get();
        
        // Check if user has a password (not Google user)
        if (user.getPassword() == null) {
            throw new AuthenticationException("Please use Google sign-in for this account");
        }
        
        // Verify password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new AuthenticationException("Invalid email or password");
        }
        
        return generateAuthResponse(user);
    }
    
    /**
     * Authenticate user with Google token
     */
    public AuthResponse authenticateGoogleUser(String googleToken) {
        try {
            // Verify Google token and get user info
            GoogleUserInfo googleUserInfo = googleOAuthService.verifyGoogleToken(googleToken);
            
            // Find or create user
            UserModel user = googleOAuthService.findOrCreateGoogleUser(googleUserInfo);
            
            return generateAuthResponse(user);
            
        } catch (Exception e) {
            log.error("Google authentication failed: {}", e.getMessage());
            throw new AuthenticationException("Google authentication failed: " + e.getMessage());
        }
    }
    
    /**
     * Refresh access token using refresh token
     */
    public AuthResponse refreshToken(String refreshToken) {
        // Remove "Bearer " prefix if present
        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }
        
        // Validate refresh token
        if (!jwtService.validateToken(refreshToken) || !jwtService.isRefreshToken(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }
        
        // Extract user info from token
        String username = jwtService.extractUsername(refreshToken);
        Optional<UserModel> userOptional = userRepository.findByUsername(username);
        
        if (userOptional.isEmpty()) {
            throw new InvalidTokenException("User not found for refresh token");
        }
        
        UserModel user = userOptional.get();
        return generateAuthResponse(user);
    }
    
    /**
     * Logout user (currently just validates token)
     */
    public void logout(String token) {
        // Remove "Bearer " prefix if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        if (!jwtService.validateToken(token)) {
            throw new InvalidTokenException("Invalid token");
        }
        
        // In a production system, you might want to:
        // 1. Add token to blacklist
        // 2. Store tokens in Redis and remove them
        // 3. Use token versioning
        
        log.info("User logged out successfully");
    }
    
    /**
     * Register a new user
     */
    public AuthResponse registerUser(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthenticationException("Email already registered");
        }
        // Check if username exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AuthenticationException("Username already exists");
        }
        // Create new user
        UserModel user = new UserModel();
        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setUsername(request.getUsername());
        user.setProfilePhoto(request.getProfilePhoto()); // base64 string
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setAuthProvider(com.ms_fisio.shared.domain.enums.AuthProvider.LOCAL);
        user.setEmailVerified(false);
        userRepository.save(user);
        return generateAuthResponse(user);
    }
    
    /**
     * Generate authentication response with tokens and user info
     */
    private AuthResponse generateAuthResponse(UserModel user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        
        UserResponseDTO userResponseDTO = new UserResponseDTO(
            user.getUserId(),
            user.getFullName(),
            user.getUsername(),
            user.getEmail(),
            user.getProfilePhoto(),
            user.getEmailVerified()
        );
        
        return new AuthResponse(accessToken, refreshToken, accessTokenExpiration, userResponseDTO);
    }
}
