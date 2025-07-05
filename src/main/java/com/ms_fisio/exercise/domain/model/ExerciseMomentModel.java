package com.ms_fisio.exercise.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Exercise moment entity for timestamped exercise events
 */
@Entity
@Table(name = "exercise_moments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseMomentModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_moment_id")
    private Long exerciseMomentId;
    
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private ExerciseModel exercise;
}
