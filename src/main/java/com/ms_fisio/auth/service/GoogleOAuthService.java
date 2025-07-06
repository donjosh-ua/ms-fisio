package com.ms_fisio.auth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms_fisio.auth.dto.GoogleUserInfo;
import com.ms_fisio.auth.exception.GoogleAuthenticationException;
import com.ms_fisio.shared.domain.enums.AuthProvider;
import com.ms_fisio.user.domain.model.UserModel;
import com.ms_fisio.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Service for Google OAuth operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthService {
    
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    private static final String GOOGLE_TOKEN_INFO_URL = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=";
    
    /**
     * Verify Google token and extract user information
     */
    public GoogleUserInfo verifyGoogleToken(String googleToken) {
        try {
            String url = GOOGLE_TOKEN_INFO_URL + googleToken;
            String response = restTemplate.getForObject(url, String.class);
            
            if (response == null) {
                throw new GoogleAuthenticationException("Invalid response from Google");
            }
            
            JsonNode jsonNode = objectMapper.readTree(response);
            
            // Check if token is valid
            if (jsonNode.has("error")) {
                throw new GoogleAuthenticationException("Invalid Google token: " + jsonNode.get("error_description").asText());
            }
            
            // Extract user information
            String googleId = jsonNode.get("sub").asText();
            String email = jsonNode.get("email").asText();
            String name = jsonNode.get("name").asText();
            String picture = jsonNode.has("picture") ? jsonNode.get("picture").asText() : null;
            Boolean emailVerified = jsonNode.get("email_verified").asBoolean();
            
            return new GoogleUserInfo(googleId, email, name, picture, emailVerified);
            
        } catch (Exception e) {
            log.error("Error verifying Google token: {}", e.getMessage());
            throw new GoogleAuthenticationException("Failed to verify Google token: " + e.getMessage());
        }
    }
    
    /**
     * Find existing user or create new user from Google info
     */
    public UserModel findOrCreateGoogleUser(GoogleUserInfo googleInfo) {
        // First, try to find user by Google ID
        Optional<UserModel> existingUser = userRepository.findByGoogleId(googleInfo.getGoogleId());
        
        if (existingUser.isPresent()) {
            return existingUser.get();
        }
        
        // Then, try to find user by email
        existingUser = userRepository.findByEmail(googleInfo.getEmail());
        
        if (existingUser.isPresent()) {
            // Update existing user with Google info
            UserModel user = existingUser.get();
            user.setGoogleId(googleInfo.getGoogleId());
            user.setAuthProvider(AuthProvider.GOOGLE);
            user.setEmailVerified(googleInfo.getEmailVerified());
            if (user.getProfilePhoto() == null && googleInfo.getPicture() != null) {
                user.setProfilePhoto(googleInfo.getPicture());
            }
            return userRepository.save(user);
        }
        
        // Create new user
        UserModel newUser = new UserModel();
        newUser.setGoogleId(googleInfo.getGoogleId());
        newUser.setEmail(googleInfo.getEmail());
        newUser.setFullName(googleInfo.getName());
        newUser.setUsername(generateUsernameFromEmail(googleInfo.getEmail()));
        newUser.setProfilePhoto(googleInfo.getPicture());
        newUser.setAuthProvider(AuthProvider.GOOGLE);
        newUser.setEmailVerified(googleInfo.getEmailVerified());
        newUser.setPassword(null); // No password for Google users
        
        return userRepository.save(newUser);
    }
    
    /**
     * Generate username from email
     */
    private String generateUsernameFromEmail(String email) {
        String baseUsername = email.substring(0, email.indexOf("@"));
        String username = baseUsername;
        int counter = 1;
        
        // Ensure username is unique
        while (userRepository.existsByUsername(username)) {
            username = baseUsername + counter;
            counter++;
        }
        
        return username;
    }
}
