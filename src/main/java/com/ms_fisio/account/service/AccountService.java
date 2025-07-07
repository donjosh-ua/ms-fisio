package com.ms_fisio.account.service;

import com.ms_fisio.account.dto.ChangePasswordRequest;
import com.ms_fisio.account.dto.UpdateAccountRequest;
import com.ms_fisio.account.exception.AccountException;
import com.ms_fisio.user.domain.model.UserModel;
import com.ms_fisio.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * Service for account management operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * Update account information
     */
    @Transactional
    public void updateAccountInformation(Long userId, UpdateAccountRequest request) {
        UserModel user = getUserById(userId);
        
        // Update username if provided
        if (StringUtils.hasText(request.getUsername())) {
            validateUsernameUniqueness(request.getUsername(), userId);
            user.setUsername(request.getUsername());
        }
        
        // Update full name if provided
        if (StringUtils.hasText(request.getFullName())) {
            user.setFullName(request.getFullName());
        }
        
        // Update email if provided
        if (StringUtils.hasText(request.getEmail())) {
            validateEmailUniqueness(request.getEmail(), userId);
            user.setEmail(request.getEmail());
        }
        
        userRepository.save(user);
        log.info("Account information updated for user: {}", userId);
    }
    
    /**
     * Change user password
     */
    @Transactional
    public void changePassword(Long userId, ChangePasswordRequest request) {
        UserModel user = getUserById(userId);
        
        // Verify current password
        if (user.getPassword() == null) {
            throw new AccountException("Cannot change password for OAuth users");
        }
        
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AccountException("Current password is incorrect");
        }
        
        // Validate new password is different
        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new AccountException("New password must be different from current password");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        
        log.info("Password changed for user: {}", userId);
    }
    
    /**
     * Delete user account
     */
    @Transactional
    public void deleteAccount(Long userId) {
        UserModel user = getUserById(userId);
        
        try {
            userRepository.delete(user);
            log.info("Account deleted for user: {}", userId);
        } catch (Exception e) {
            log.error("Failed to delete account for user: {}", userId, e);
            throw new AccountException("Unable to delete account at this time");
        }
    }
    
    /**
     * Get user by ID or throw exception
     */
    private UserModel getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AccountException("User not found"));
    }
    
    /**
     * Validate username uniqueness
     */
    private void validateUsernameUniqueness(String username, Long currentUserId) {
        Optional<UserModel> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent() && !existingUser.get().getUserId().equals(currentUserId)) {
            throw new AccountException("Username already exists");
        }
    }
    
    /**
     * Validate email uniqueness
     */
    private void validateEmailUniqueness(String email, Long currentUserId) {
        Optional<UserModel> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent() && !existingUser.get().getUserId().equals(currentUserId)) {
            throw new AccountException("Email already exists");
        }
    }
}
