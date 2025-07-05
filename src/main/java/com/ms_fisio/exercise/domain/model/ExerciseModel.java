package com.ms_fisio.exercise.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.ms_fisio.routine.domain.model.RoutineExerciseModel;
import com.ms_fisio.session.domain.model.ExercisePerformanceModel;

/**
 * Exercise entity representing individual exercises
 */
@Entity
@Table(name = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id")
    private Long exerciseId;
    
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    @Column(name = "video_url", length = 255)
    private String videoUrl;
    
    @Column(name = "sets")
    private Integer sets;
    
    @Column(name = "reps_per_set")
    private Integer repsPerSet;
    
    @Column(name = "with_assistant", nullable = false)
    private Boolean withAssistant;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    // Relationships
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoutineExerciseModel> routineExercises;
    
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExerciseMomentModel> exerciseMoments;
    
    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExercisePerformanceModel> exercisePerformances;
}
