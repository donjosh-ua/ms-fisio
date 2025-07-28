package com.ms_fisio.session.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ms_fisio.session.domain.model.FeedbackModel;
import com.ms_fisio.session.domain.dto.FeedbackChartDTO;
import com.ms_fisio.session.domain.dto.SessionChartDTO;

/**
 * Repository interface for Feedback entity
 */
@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackModel, Long> {

    @Query("""
        SELECT new com.ms_fisio.session.domain.dto.SessionChartDTO(
            1,
            CASE 
                WHEN f.calification >= 4 THEN 1  -- Positivo
                WHEN f.calification = 3 THEN 2   -- Neutro
                ELSE 3                           -- Negativo
            END,
            COUNT(f)
        )
        FROM FeedbackModel f
        WHERE f.routineSession.routine.createdByUser.userId = :userId
        GROUP BY 
            CASE 
                WHEN f.calification >= 4 THEN 1
                WHEN f.calification = 3 THEN 2
                ELSE 3
            END
    """)
    List<SessionChartDTO> countFeedbackBySentiment(Long userId);

}
