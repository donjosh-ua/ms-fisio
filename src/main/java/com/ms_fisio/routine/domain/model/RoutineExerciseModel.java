package com.ms_fisio.routine.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ms_fisio.exercise.domain.model.ExerciseModel;

/**
 * Junction table between routines and exercises
 */
@Entity
@Table(name = "routine_exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineExerciseModel {
    
    @EmbeddedId
    private RoutineExerciseId routineExerciseId;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("routineId")
    @JoinColumn(name = "routine_id", nullable = false)
    private RoutineModel routine;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("exerciseId")
    @JoinColumn(name = "exercise_id", nullable = false)
    private ExerciseModel exercise;
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RoutineExerciseId {
        @Column(name = "routine_id")
        private Long routineId;
        
        @Column(name = "exercise_id")
        private Long exerciseId;
    }
}
