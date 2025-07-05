package com.ms_fisio.routine.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ms_fisio.shared.domain.enums.DayOfWeek;

/**
 * Routine day entity for scheduling routines on specific days
 */
@Entity
@Table(name = "routine_days")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineDayModel {
    
    @EmbeddedId
    private RoutineDayId routineDayId;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("routineId")
    @JoinColumn(name = "routine_id", nullable = false)
    private RoutineModel routine;
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoutineDayId {
        @Column(name = "routine_id")
        private Long routineId;
        
        @Enumerated(EnumType.STRING)
        @Column(name = "day_of_week", length = 10)
        private DayOfWeek dayOfWeek;
    }
}
