package com.ms_fisio.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ms_fisio.session.domain.model.RoutineSessionModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for RoutineSession entity
 */
@Repository
public interface RoutineSessionRepository extends JpaRepository<RoutineSessionModel, Long> {
    
    /**
     * Find active session by access code (not expired)
     */
    @Query("SELECT rs FROM RoutineSessionModel rs WHERE rs.accessCode = :accessCode AND (rs.endDatetime IS NULL OR rs.endDatetime > :currentTime)")
    Optional<RoutineSessionModel> findActiveByAccessCode(@Param("accessCode") String accessCode, @Param("currentTime") LocalDateTime currentTime);
    
    /**
     * Find all active sessions for a user (via routine creator)
     */
    @Query("SELECT rs FROM RoutineSessionModel rs WHERE rs.routine.createdByUser.userId = :userId AND (rs.endDatetime IS NULL OR rs.endDatetime > :currentTime)")
    List<RoutineSessionModel> findActiveSessionsByUserId(@Param("userId") Long userId, @Param("currentTime") LocalDateTime currentTime);
    
    /**
     * Find session by access code (regardless of expiration)
     */
    Optional<RoutineSessionModel> findByAccessCode(String accessCode);
}
