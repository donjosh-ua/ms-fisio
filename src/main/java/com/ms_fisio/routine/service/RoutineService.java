package com.ms_fisio.routine.service;

import com.ms_fisio.routine.dto.*;
import com.ms_fisio.exercise.domain.dto.ExerciseDTO;
import com.ms_fisio.exercise.domain.dto.ExerciseMomentDTO;
import com.ms_fisio.exercise.domain.dto.ObjectiveAreaDTO;
import com.ms_fisio.routine.domain.dto.CreateRoutineRequest;
import com.ms_fisio.routine.domain.dto.RoutineDTO;
import com.ms_fisio.routine.domain.model.RoutineModel;
import com.ms_fisio.routine.domain.model.RoutineDayModel;
import com.ms_fisio.routine.domain.model.RoutineExerciseModel;
import com.ms_fisio.routine.repository.RoutineRepository;
import com.ms_fisio.routine.repository.RoutineDayRepository;
import com.ms_fisio.routine.repository.RoutineExerciseRepository;
import com.ms_fisio.exercise.domain.model.ObjectiveAreaModel;
import com.ms_fisio.exercise.domain.model.ExerciseModel;
import com.ms_fisio.exercise.domain.model.ExerciseMomentModel;
import com.ms_fisio.exercise.repository.ObjectiveAreaRepository;
import com.ms_fisio.exercise.repository.ExerciseRepository;
import com.ms_fisio.exercise.repository.ExerciseMomentRepository;
import com.ms_fisio.user.domain.model.UserModel;
import com.ms_fisio.user.repository.UserRepository;
import com.ms_fisio.shared.domain.enums.DayOfWeek;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Service for routine operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RoutineService {

    private final RoutineRepository routineRepository;
    private final RoutineDayRepository routineDayRepository;
    private final RoutineExerciseRepository routineExerciseRepository;
    private final ObjectiveAreaRepository objectiveAreaRepository;
    private final ExerciseRepository exerciseRepository;
    private final ExerciseMomentRepository exerciseMomentRepository;
    private final UserRepository userRepository;

    /**
     * Create a new routine
     */
    @Transactional
    public RoutineResponse createRoutine(CreateRoutineRequest request, Long userId) {
        log.info("Creating new routine: {} for user: {}", request.getName(), userId);

        try {
            // Validate user exists
            UserModel user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // Validate target area exists
            ObjectiveAreaModel targetArea = objectiveAreaRepository.findById(request.getTargetAreaId())
                    .orElseThrow(() -> new RuntimeException("Target area not found"));

            // Parse difficulty
            Integer difficulty = request.getDifficulty();

            // Create routine
            RoutineModel routine = new RoutineModel();
            routine.setName(request.getName());
            routine.setCategory(request.getCategory());
            routine.setDescription(request.getDescription());
            routine.setDifficulty(difficulty);
            routine.setDuration(request.getDuration());
            routine.setWeeks(request.getWeeks());
            routine.setCreatedByUser(user);
            routine.setObjectiveArea(targetArea);

            routine = routineRepository.save(routine);

            // Save routine days
            if (request.getDays() != null && !request.getDays().isEmpty()) {
                saveDaysForRoutine(routine, request.getDays());
            }

            // Save exercises
            if (request.getExercises() != null && !request.getExercises().isEmpty()) {
                saveExercisesForRoutine(routine, request.getExercises());
            }

            // Generate session code
            String sessionCode = generateSessionCode();

            log.info("Routine created successfully with ID: {}", routine.getRoutineId());
            return RoutineResponse.success("Routine saved successfully.", sessionCode);

        } catch (Exception e) {
            log.error("Error creating routine: {}", e.getMessage());
            return RoutineResponse.error("Failed to create routine: " + e.getMessage());
        }
    }

    /**
     * Update an existing routine
     */
    @Transactional
    public RoutineResponse updateRoutine(Long routineId, CreateRoutineRequest request, Long userId) {
        log.info("Updating routine: {} for user: {}", routineId, userId);
        try {
            RoutineModel routine = routineRepository.findById(routineId)
                    .orElseThrow(() -> new RuntimeException("Routine not found"));
            if (!routine.getCreatedByUser().getUserId().equals(userId)) {
                throw new RuntimeException("Unauthorized to modify this routine");
            }
            ObjectiveAreaModel targetArea = objectiveAreaRepository.findById(request.getTargetAreaId())
                    .orElseThrow(() -> new RuntimeException("Target area not found"));
            Integer difficulty = request.getDifficulty();
            routine.setName(request.getName());
            routine.setCategory(request.getCategory());
            routine.setDescription(request.getDescription());
            routine.setDifficulty(difficulty);
            routine.setDuration(request.getDuration());
            routine.setWeeks(request.getWeeks());
            routine.setObjectiveArea(targetArea);
            // Eliminar ejercicios que ya no están en la lista enviada
            List<Long> idsEnviados = request.getExercises().stream()
                .map(e -> e.getExerciseId())
                .filter(id -> id != null)
                .toList();
            List<RoutineExerciseModel> ejerciciosActuales = routine.getRoutineExercises();
            // Si la lista enviada está vacía, elimina todos los ejercicios
            if (idsEnviados.isEmpty()) {
                for (RoutineExerciseModel re : ejerciciosActuales) {
                    routineExerciseRepository.delete(re);
                }
                ejerciciosActuales.clear();
                routine.setRoutineExercises(ejerciciosActuales);
            } else {
                // Elimina solo los que no están en la lista enviada
                ejerciciosActuales.removeIf(re -> {
                    boolean eliminar = !idsEnviados.contains(re.getExercise().getExerciseId());
                    if (eliminar) routineExerciseRepository.delete(re);
                    return eliminar;
                });
                routine.setRoutineExercises(ejerciciosActuales);
            }
            // Clear existing relationships and create new ones
            clearRoutineRelationships(routine);
            // Save updated routine
            routine = routineRepository.save(routine);
            // Save new routine days
            if (request.getDays() != null && !request.getDays().isEmpty()) {
                saveDaysForRoutine(routine, request.getDays());
            }
            // Save new exercises
            if (request.getExercises() != null && !request.getExercises().isEmpty()) {
                saveExercisesForRoutine(routine, request.getExercises());
            }
            // Generate session code
            String sessionCode = generateSessionCode();
            log.info("Routine updated successfully with ID: {}", routine.getRoutineId());
            return RoutineResponse.success("Routine saved successfully.", sessionCode);
        } catch (Exception e) {
            log.error("Error updating routine: {}", e.getMessage());
            return RoutineResponse.error("Failed to update routine: " + e.getMessage());
        }
    }

    /**
     * Delete a routine
     */
    @Transactional
    public RoutineResponse deleteRoutine(Long routineId, Long userId) {
        log.info("Deleting routine: {} for user: {}", routineId, userId);

        try {
            RoutineModel routine = routineRepository.findById(routineId)
                    .orElseThrow(() -> new RuntimeException("Routine not found"));

            // Validate user owns the routine
            if (!routine.getCreatedByUser().getUserId().equals(userId)) {
                throw new RuntimeException("Unauthorized to delete this routine");
            }

            routineRepository.delete(routine);

            log.info("Routine deleted successfully with ID: {}", routineId);
            return RoutineResponse.success("Routine deleted successfully.");

        } catch (Exception e) {
            log.error("Error deleting routine: {}", e.getMessage());
            return RoutineResponse.error("Failed to delete routine: " + e.getMessage());
        }
    }

    /**
     * Get all routines for a user
     */
    public RoutinesResponse getAllRoutines(Long userId) {
        log.info("Fetching all routines for user: {}", userId);

        try {
            // Fetch routines directly by user ID to avoid lazy loading issues
            List<RoutineModel> routines = routineRepository.findByCreatedByUser_UserId(userId);

            List<RoutineSummaryDto> routineSummaries = routines.stream()
                    .map(routine -> new RoutineSummaryDto(
                            routine.getRoutineId().toString(),
                            routine.getName(),
                            routine.getCategory(),
                            routine.getDuration(),
                            routine.getDifficulty()))
                    .toList();

            return new RoutinesResponse(routineSummaries);

        } catch (Exception e) {
            log.error("Error fetching routines: {}", e.getMessage());
            return new RoutinesResponse(List.of());
        }
    }

    /**
     * Get a routine by ID
     */
    @Transactional(readOnly = true)
    public RoutineResponse getRoutineById(Long routineId, Long userId) {
        log.info("Fetching routine by id: {} for user: {}", routineId, userId);
        try {
            RoutineModel routine = routineRepository.findById(routineId)
                    .orElseThrow(() -> new RuntimeException("Routine not found"));
            if (!routine.getCreatedByUser().getUserId().equals(userId)) {
                throw new RuntimeException("Unauthorized to view this routine");
            }
            // Map RoutineModel to RoutineDTO
            RoutineDTO dto = new RoutineDTO();
            dto.setRoutineId(routine.getRoutineId());
            dto.setName(routine.getName());
            dto.setCategory(routine.getCategory());
            dto.setDescription(routine.getDescription());
            dto.setDifficulty(routine.getDifficulty());
            dto.setDuration(routine.getDuration());
            dto.setWeeks(routine.getWeeks());
            // ObjectiveArea
            if (routine.getObjectiveArea() != null) {
                ObjectiveAreaDTO areaDTO = new ObjectiveAreaDTO();
                areaDTO.setId(routine.getObjectiveArea().getObjectiveAreaId());
                areaDTO.setName(routine.getObjectiveArea().getName());
                areaDTO.setDescription(""); // Add description if available
                dto.setObjectiveArea(areaDTO);
            }
            // Days
            if (routine.getRoutineDays() != null) {
                dto.setDays(routine.getRoutineDays().stream()
                    .map(d -> d.getRoutineDayId().getDayOfWeek().name())
                    .toList());
            }
            // Exercises
            if (routine.getRoutineExercises() != null) {
                dto.setExercises(routine.getRoutineExercises().stream().map(re -> {
                    ExerciseModel ex = re.getExercise();
                    ExerciseDTO exDto = new ExerciseDTO();
                    exDto.setExerciseId(ex.getExerciseId());
                    exDto.setName(ex.getName());
                    exDto.setDescription(ex.getDescription());
                    exDto.setVideoUrl(ex.getVideoUrl());
                    exDto.setVideoId(ex.getVideoId());
                    exDto.setSets(ex.getSets());
                    exDto.setRepetitions(ex.getRepsPerSet());
                    exDto.setAssisted(ex.getWithAssistant());
                    if (ex.getObjectiveArea() != null) {
                        exDto.setObjectiveAreaId(ex.getObjectiveArea().getObjectiveAreaId());
                    }
                    if (ex.getExerciseMoments() != null) {
                        exDto.setKeyMoments(ex.getExerciseMoments().stream().map(m -> {
                            ExerciseMomentDTO mdto = new ExerciseMomentDTO();
                            mdto.setId(m.getExerciseMomentId());
                            mdto.setDescription(m.getDescription());
                            mdto.setTimestamp(m.getTimestamp());
                            return mdto;
                        }).toList());
                    }
                    return exDto;
                }).toList());
            }
            return RoutineResponse.success("Routine fetched successfully.", dto);
        } catch (Exception e) {
            log.error("Error fetching routine: {}", e.getMessage());
            return RoutineResponse.error("Failed to fetch routine: " + e.getMessage());
        }
    }

    /**
     * Save days for a routine
     */
    private void saveDaysForRoutine(RoutineModel routine, List<String> days) {
        for (String day : days) {
            try {
                DayOfWeek dayOfWeek = mapDayToEnum(day);

                RoutineDayModel routineDay = new RoutineDayModel();
                RoutineDayModel.RoutineDayId routineDayId = new RoutineDayModel.RoutineDayId();
                routineDayId.setRoutineId(routine.getRoutineId());
                routineDayId.setDayOfWeek(dayOfWeek);

                routineDay.setRoutineDayId(routineDayId);
                routineDay.setRoutine(routine);

                routineDayRepository.save(routineDay);
            } catch (Exception e) {
                log.warn("Failed to save day {} for routine {}: {}", day, routine.getRoutineId(), e.getMessage());
            }
        }
    }

    /**
     * Save exercises for a routine
     */
    private void saveExercisesForRoutine(RoutineModel routine, List<ExerciseDto> exercises) {
        for (ExerciseDto exerciseDto : exercises) {
            try {
                ExerciseModel exercise;
                if (exerciseDto.getExerciseId() != null) {
                    // Buscar ejercicio existente
                    exercise = exerciseRepository.findById(exerciseDto.getExerciseId()).orElse(null);
                    if (exercise != null) {
                        // Actualizar datos del ejercicio existente
                        exercise.setName(exerciseDto.getName());
                        exercise.setVideoUrl(exerciseDto.getVideoUrl());
                        exercise.setVideoId(exerciseDto.getVideoId());
                        exercise.setSets(exerciseDto.getSets());
                        exercise.setRepsPerSet(exerciseDto.getRepetitions());
                        exercise.setWithAssistant(Boolean.TRUE.equals(exerciseDto.getAssisted()));
                        exercise.setDescription(exerciseDto.getDescription());
                        exercise = exerciseRepository.save(exercise);
                    } else {
                        // Si el id no existe, crear nuevo
                        exercise = new ExerciseModel();
                        exercise.setName(exerciseDto.getName());
                        exercise.setVideoUrl(exerciseDto.getVideoUrl());
                        exercise.setVideoId(exerciseDto.getVideoId());
                        exercise.setSets(exerciseDto.getSets());
                        exercise.setRepsPerSet(exerciseDto.getRepetitions());
                        exercise.setWithAssistant(Boolean.TRUE.equals(exerciseDto.getAssisted()));
                        exercise.setDescription(exerciseDto.getDescription());
                        exercise = exerciseRepository.save(exercise);
                    }
                } else {
                    // Crear nuevo ejercicio
                    exercise = new ExerciseModel();
                    exercise.setName(exerciseDto.getName());
                    exercise.setVideoUrl(exerciseDto.getVideoUrl());
                    exercise.setVideoId(exerciseDto.getVideoId());
                    exercise.setSets(exerciseDto.getSets());
                    exercise.setRepsPerSet(exerciseDto.getRepetitions());
                    exercise.setWithAssistant(Boolean.TRUE.equals(exerciseDto.getAssisted()));
                    exercise.setDescription(exerciseDto.getDescription());
                    exercise = exerciseRepository.save(exercise);
                }

                // Crear relación entre rutina y ejercicio
                RoutineExerciseModel routineExercise = new RoutineExerciseModel();
                RoutineExerciseModel.RoutineExerciseId routineExerciseId = new RoutineExerciseModel.RoutineExerciseId();
                routineExerciseId.setRoutineId(routine.getRoutineId());
                routineExerciseId.setExerciseId(exercise.getExerciseId());

                routineExercise.setRoutineExerciseId(routineExerciseId);
                routineExercise.setRoutine(routine);
                routineExercise.setExercise(exercise);

                routineExerciseRepository.save(routineExercise);

                // Guardar key moments
                if (exerciseDto.getKeyMoments() != null) {
                    saveKeyMomentsForExercise(exercise, exerciseDto.getKeyMoments());
                }

            } catch (Exception e) {
                log.warn("Failed to save exercise {} for routine {}: {}", exerciseDto.getName(), routine.getRoutineId(),
                        e.getMessage());
            }
        }
    }

    /**
     * Save key moments for an exercise
     */
    private void saveKeyMomentsForExercise(ExerciseModel exercise, List<KeyMomentDto> keyMoments) {
        for (KeyMomentDto keyMoment : keyMoments) {
            try {
                ExerciseMomentModel moment = new ExerciseMomentModel();
                moment.setTimestamp(keyMoment.getTimestamp());
                moment.setDescription(keyMoment.getDescription());
                moment.setExercise(exercise);

                exerciseMomentRepository.save(moment);
            } catch (Exception e) {
                log.warn("Failed to save key moment for exercise {}: {}", exercise.getExerciseId(), e.getMessage());
            }
        }
    }

    /**
     * Clear existing routine relationships
     */
    private void clearRoutineRelationships(RoutineModel routine) {
        // This would require custom queries to efficiently delete relationships
        // For simplicity, we're letting JPA cascade handle deletions
        log.debug("Clearing relationships for routine: {}", routine.getRoutineId());
    }

    /**
     * Map day string to DayOfWeek enum
     */
    private DayOfWeek mapDayToEnum(String day) {
        return switch (day.toLowerCase()) {
            case "lunes", "monday" -> DayOfWeek.MONDAY;
            case "martes", "tuesday" -> DayOfWeek.TUESDAY;
            case "miércoles", "miercoles", "wednesday" -> DayOfWeek.WEDNESDAY;
            case "jueves", "thursday" -> DayOfWeek.THURSDAY;
            case "viernes", "friday" -> DayOfWeek.FRIDAY;
            case "sábado", "sabado", "saturday" -> DayOfWeek.SATURDAY;
            case "domingo", "sunday" -> DayOfWeek.SUNDAY;
            default -> throw new IllegalArgumentException("Invalid day: " + day);
        };
    }

    /**
     * Generate a unique session code
     */
    private String generateSessionCode() {
        return "chx-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
