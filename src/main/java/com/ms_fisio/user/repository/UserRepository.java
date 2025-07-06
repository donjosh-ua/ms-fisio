package com.ms_fisio.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ms_fisio.user.domain.model.UserModel;

import java.util.Optional;

/**
 * Repository interface for User entity
 */
@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    
    /**
     * Find user by email
     */
    Optional<UserModel> findByEmail(String email);
    
    /**
     * Find user by username
     */
    Optional<UserModel> findByUsername(String username);
    
    /**
     * Find user by Google ID
     */
    Optional<UserModel> findByGoogleId(String googleId);
    
    /**
     * Check if username exists
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if email exists
     */
    boolean existsByEmail(String email);
}
