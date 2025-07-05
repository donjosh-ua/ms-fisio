package com.ms_fisio.session.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ms_fisio.exercise.domain.model.ExerciseModel;

/**
 * Exercise performance entity for tracking exercise compliance
 */
@Entity
@Table(name = "exercise_performances")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExercisePerformanceModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_performance_id")
    private Long exercisePerformanceId;
    
    @Column(name = "posture_compliance")
    private Float postureCompliance;
    
    @Column(name = "repetition_compliance")
    private Float repetitionCompliance;
    
    @Column(name = "image_captures", columnDefinition = "TEXT")
    private String imageCaptures;
    
    @Column(name = "compliance_level", length = 20)
    private String complianceLevel;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_id", nullable = false)
    private ExerciseModel exercise;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private RoutineSessionModel routineSession;
}
