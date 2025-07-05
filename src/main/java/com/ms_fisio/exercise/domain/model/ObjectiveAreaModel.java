package com.ms_fisio.exercise.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import com.ms_fisio.routine.domain.model.RoutineModel;

/**
 * Objective area entity for categorizing routines
 */
@Entity
@Table(name = "objective_areas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObjectiveAreaModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "objective_area_id")
    private Long objectiveAreaId;
    
    @Column(name = "name", length = 100, nullable = false)
    private String name;
    
    // Relationships
    @OneToMany(mappedBy = "objectiveArea", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RoutineModel> routines;
}
