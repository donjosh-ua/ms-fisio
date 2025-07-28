package com.ms_fisio.session.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ms_fisio.session.domain.model.FeedbackModel;
import com.ms_fisio.session.domain.dto.BarChartDTO;
import com.ms_fisio.session.domain.dto.FeedbackCommentaryDTO;

/**
 * Repository interface for Feedback entity
 */
@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackModel, Long> {

    @Query("""
        SELECT
            CASE
                WHEN f.calification >= 4 THEN 1
                WHEN f.calification = 3 THEN 2
                ELSE 3
            END AS calification,
            COUNT(f) AS count
        FROM FeedbackModel f
        WHERE f.routineSession.routine.createdByUser.userId = :userId
        GROUP BY
            CASE
                WHEN f.calification >= 4 THEN 1
                WHEN f.calification = 3 THEN 2
                ELSE 3
            END
        """)
    List<BarChartDTO> countFeedbackBySentiment(@Param("userId") Long userId);

    @Query("""
        SELECT
            'commentary' AS type,
            r.name AS senderName,
            f.feedback AS message,
            CURRENT_TIMESTAMP AS sentAt
        FROM FeedbackModel f
        JOIN f.routineSession s
        JOIN s.routine r
        WHERE r.createdByUser.userId = :userId
          AND f.feedback IS NOT NULL AND f.feedback <> ''
        """)
    List<FeedbackCommentaryDTO> findCommentsByRoutineCreator(@Param("userId") Long userId);

}