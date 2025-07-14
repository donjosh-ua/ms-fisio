package com.ms_fisio.exercise.controller;

import com.ms_fisio.exercise.domain.dto.CreateExerciseRequest;
import com.ms_fisio.exercise.domain.dto.ExerciseDTO;
import com.ms_fisio.exercise.domain.dto.ExerciseMomentDTO;
import com.ms_fisio.exercise.domain.dto.UpdateExerciseRequest;
import com.ms_fisio.exercise.domain.model.ExerciseModel;
import com.ms_fisio.exercise.domain.model.ExerciseMomentModel;
import com.ms_fisio.exercise.domain.model.ObjectiveAreaModel;
import com.ms_fisio.exercise.repository.ExerciseMomentRepository;
import com.ms_fisio.exercise.repository.ExerciseRepository;
import com.ms_fisio.exercise.repository.ObjectiveAreaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/exercise")
@RequiredArgsConstructor
public class ExerciseController {
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMomentRepository exerciseMomentRepository;
    private final ObjectiveAreaRepository objectiveAreaRepository;

    @GetMapping
    @Transactional(readOnly = true)
    public ResponseEntity<List<ExerciseDTO>> getAllExercises() {
        List<ExerciseModel> exercises = exerciseRepository.findAll();
        // Inicializa manualmente las colecciones para evitar LazyInitializationException
        exercises.forEach(e -> {
            if (e.getExerciseMoments() != null) {
                e.getExerciseMoments().size();
            }
            if (e.getObjectiveArea() != null) {
                e.getObjectiveArea().getObjectiveAreaId();
            }
        });
        List<ExerciseDTO> dtos = exercises.stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public ResponseEntity<ExerciseDTO> getExercise(@PathVariable Long id) {
        Optional<ExerciseModel> opt = exerciseRepository.findById(id);
        if (opt.isPresent()) {
            ExerciseModel e = opt.get();
            if (e.getExerciseMoments() != null) {
                e.getExerciseMoments().size();
            }
            if (e.getObjectiveArea() != null) {
                e.getObjectiveArea().getObjectiveAreaId();
            }
            return ResponseEntity.ok(toDto(e));
        }
        return ResponseEntity.notFound().build();
    }

    private ExerciseDTO toDto(ExerciseModel exercise) {
        ExerciseDTO dto = new ExerciseDTO();
        dto.setExerciseId(exercise.getExerciseId());
        dto.setName(exercise.getName());
        dto.setDescription(exercise.getDescription());
        dto.setVideoUrl(exercise.getVideoUrl());
        dto.setVideoId(exercise.getVideoId());
        dto.setSets(exercise.getSets());
        dto.setRepetitions(exercise.getRepsPerSet());
        dto.setAssisted(exercise.getWithAssistant());
        if (exercise.getObjectiveArea() != null) {
            dto.setObjectiveAreaId(exercise.getObjectiveArea().getObjectiveAreaId());
        }
        if (exercise.getExerciseMoments() != null) {
            dto.setKeyMoments(exercise.getExerciseMoments().stream().map(m -> {
                ExerciseMomentDTO mdto = new ExerciseMomentDTO();
                mdto.setId(m.getExerciseMomentId());
                mdto.setDescription(m.getDescription());
                mdto.setTimestamp(m.getTimestamp());
                return mdto;
            }).collect(Collectors.toList()));
        }
        return dto;
    }

    @PostMapping
    public ResponseEntity<ExerciseDTO> createExercise(@RequestBody CreateExerciseRequest request) {
        ExerciseModel exercise = new ExerciseModel();
        exercise.setName(request.getName());
        exercise.setDescription(request.getDescription());
        exercise.setVideoUrl(request.getVideoUrl());
        exercise.setVideoId(request.getVideoId());
        exercise.setSets(request.getSets());
        exercise.setRepsPerSet(request.getRepetitions());
        exercise.setWithAssistant(request.getAssisted());
        // Asignar ObjectiveArea
        if (request.getObjectiveAreaId() != null) {
            ObjectiveAreaModel area = objectiveAreaRepository.findById(request.getObjectiveAreaId()).orElse(null);
            if (area != null) {
                exercise.setObjectiveArea(area);
            }
        }
        // Asignar ExerciseMoments
        if (request.getKeyMoments() != null && !request.getKeyMoments().isEmpty()) {
            List<ExerciseMomentModel> moments = request.getKeyMoments().stream().map(dto -> {
                ExerciseMomentModel moment = new ExerciseMomentModel();
                moment.setTimestamp(dto.getTimestamp());
                moment.setDescription(dto.getDescription());
                moment.setExercise(exercise);
                return moment;
            }).toList();
            exercise.setExerciseMoments(moments);
        }
        ExerciseModel saved = exerciseRepository.save(exercise);
        if (saved.getExerciseMoments() != null) {
            saved.getExerciseMoments().forEach(exerciseMomentRepository::save);
        }
        return ResponseEntity.ok(toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExerciseDTO> updateExercise(@PathVariable Long id, @RequestBody UpdateExerciseRequest request) {
        Optional<ExerciseModel> optionalExercise = exerciseRepository.findById(id);
        if (optionalExercise.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ExerciseModel exercise = optionalExercise.get();
        exercise.setName(request.getName());
        exercise.setDescription(request.getDescription());
        exercise.setVideoUrl(request.getVideoUrl());
        exercise.setVideoId(request.getVideoId());
        exercise.setSets(request.getSets());
        exercise.setRepsPerSet(request.getRepetitions());
        exercise.setWithAssistant(request.getAssisted());
        // Actualizar ObjectiveArea
        if (request.getObjectiveAreaId() != null) {
            ObjectiveAreaModel area = objectiveAreaRepository.findById(request.getObjectiveAreaId()).orElse(null);
            if (area != null) {
                exercise.setObjectiveArea(area);
            }
        }
        // Actualizar ExerciseMoments
        if (request.getKeyMoments() != null) {
            // Elimina los momentos anteriores
            if (exercise.getExerciseMoments() != null) {
                exercise.getExerciseMoments().forEach(exerciseMomentRepository::delete);
            }
            List<ExerciseMomentModel> moments = request.getKeyMoments().stream().map(dto -> {
                ExerciseMomentModel moment = new ExerciseMomentModel();
                moment.setTimestamp(dto.getTimestamp());
                moment.setDescription(dto.getDescription());
                moment.setExercise(exercise);
                return moment;
            }).toList();
            exercise.setExerciseMoments(moments);
        }
        ExerciseModel saved = exerciseRepository.save(exercise);
        if (saved.getExerciseMoments() != null) {
            saved.getExerciseMoments().forEach(exerciseMomentRepository::save);
        }
        return ResponseEntity.ok(toDto(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExercise(@PathVariable Long id) {
        Optional<ExerciseModel> exercise = exerciseRepository.findById(id);
        if (exercise.isPresent()) {
            if (exercise.get().getExerciseMoments() != null) {
                exercise.get().getExerciseMoments().forEach(exerciseMomentRepository::delete);
            }
            exerciseRepository.deleteById(id);
        }
        return ResponseEntity.noContent().build();
    }
}
