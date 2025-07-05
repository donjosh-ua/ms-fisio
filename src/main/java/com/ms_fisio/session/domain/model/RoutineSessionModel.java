package com.ms_fisio.session.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import com.ms_fisio.routine.domain.model.RoutineModel;
import com.ms_fisio.patient.domain.model.PatientRoutineModel;

/**
 * Routine session entity representing live workout sessions
 */
@Entity
@Table(name = "routine_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineSessionModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_session_id")
    private Long routineSessionId;
    
    @Column(name = "access_code", length = 50, unique = true)
    private String accessCode;
    
    @Column(name = "start_datetime")
    private LocalDateTime startDatetime;
    
    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;
    
    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private RoutineModel routine;
    
    @OneToMany(mappedBy = "routineSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ExercisePerformanceModel> exercisePerformances;
    
    @OneToMany(mappedBy = "routineSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FeedbackModel> feedbacks;
    
    @OneToMany(mappedBy = "routineSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PatientRoutineModel> patientRoutines;
}
