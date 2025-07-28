package com.ms_fisio.session.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ms_fisio.session.domain.dto.SessionChartDTO;
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

    @Query(value = """
        SELECT 
            EXTRACT(YEAR FROM rs.start_datetime) AS year,
            EXTRACT(MONTH FROM rs.start_datetime) AS month,
            COUNT(*) AS count
        FROM routine_sessions rs
        JOIN routines r ON rs.routine_id = r.routine_id
        WHERE r.created_by_user_id = :userId
        GROUP BY year, month
        ORDER BY year, month
        """, nativeQuery = true)
    List<SessionChartDTO> countPlannedSessionsByStartDate(
        @Param("userId") Long userId
    );

    @Query(value = """
        SELECT 
            EXTRACT(YEAR FROM rs.start_datetime) AS year,
            EXTRACT(MONTH FROM rs.start_datetime) AS month,
            COUNT(DISTINCT rs.routine_session_id) AS count
        FROM routine_sessions rs
        JOIN exercise_performances ep ON ep.session_id = rs.routine_session_id
        JOIN routines r ON rs.routine_id = r.routine_id
        WHERE r.created_by_user_id = :userId
        AND rs.end_datetime < :now
        GROUP BY year, month
        ORDER BY year, month
        """, nativeQuery = true)
    List<SessionChartDTO> countExecutedSessionsByStartDate(
        @Param("userId") Long userId,
        @Param("now") LocalDateTime now
    );


}
