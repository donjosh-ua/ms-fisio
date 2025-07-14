package com.ms_fisio.routine.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.ms_fisio.user.domain.model.UserModel;
import com.ms_fisio.exercise.domain.model.ObjectiveAreaModel;
import com.ms_fisio.session.domain.model.RoutineSessionModel;

/**
 * Routine entity representing exercise routines
 */
@Entity
@Table(name = "routines")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoutineModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id")
    private Long routineId;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "category", length = 50)
    private String category;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "difficulty", nullable = false)
    private Integer difficulty;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "weeks")
    private Integer weeks;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private UserModel createdByUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "objective_area_id")
    private ObjectiveAreaModel objectiveArea;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoutineDayModel> routineDays;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoutineExerciseModel> routineExercises;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoutineSessionModel> routineSessions;
}
