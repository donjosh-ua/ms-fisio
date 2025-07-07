package com.ms_fisio.account.controller;

import com.ms_fisio.account.dto.AccountResponse;
import com.ms_fisio.account.dto.ChangePasswordRequest;
import com.ms_fisio.account.dto.UpdateAccountRequest;
import com.ms_fisio.account.exception.AccountException;
import com.ms_fisio.account.service.AccountService;
import com.ms_fisio.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Controller for account management endpoints
 */
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AccountController {
    
    private final AccountService accountService;
    private final JwtService jwtService;
    
    /**
     * Update account information
     */
    @PatchMapping
    public ResponseEntity<AccountResponse> updateAccount(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody UpdateAccountRequest request) {
        
        try {
            Long userId = extractUserIdFromToken(authorizationHeader);
            accountService.updateAccountInformation(userId, request);
            
            return ResponseEntity.ok(AccountResponse.success("Account information updated successfully."));
            
        } catch (AccountException e) {
            return ResponseEntity.badRequest().body(AccountResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error updating account", e);
            return ResponseEntity.badRequest().body(AccountResponse.error("Unable to update account information"));
        }
    }
    
    /**
     * Change password
     */
    @PostMapping("/change-password")
    public ResponseEntity<AccountResponse> changePassword(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody ChangePasswordRequest request) {
        
        try {
            Long userId = extractUserIdFromToken(authorizationHeader);
            accountService.changePassword(userId, request);
            
            return ResponseEntity.ok(AccountResponse.success("Password changed successfully."));
            
        } catch (AccountException e) {
            return ResponseEntity.badRequest().body(AccountResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error changing password", e);
            return ResponseEntity.badRequest().body(AccountResponse.error("Unable to change password"));
        }
    }
    
    /**
     * Delete account
     */
    @DeleteMapping
    public ResponseEntity<AccountResponse> deleteAccount(
            @RequestHeader("Authorization") String authorizationHeader) {
        
        try {
            Long userId = extractUserIdFromToken(authorizationHeader);
            accountService.deleteAccount(userId);
            
            return ResponseEntity.ok(AccountResponse.success("Account deleted successfully."));
            
        } catch (AccountException e) {
            return ResponseEntity.badRequest().body(AccountResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error deleting account", e);
            return ResponseEntity.badRequest().body(AccountResponse.error("Unable to delete account at this time"));
        }
    }
    
    /**
     * Extract user ID from JWT token
     */
    private Long extractUserIdFromToken(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return jwtService.extractUserId(token);
    }
}
