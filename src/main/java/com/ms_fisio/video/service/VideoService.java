package com.ms_fisio.video.service;

import com.ms_fisio.video.dto.VideoDto;
import com.ms_fisio.video.dto.VideosResponse;
import com.ms_fisio.exercise.domain.model.ExerciseModel;
import com.ms_fisio.exercise.repository.ExerciseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for video operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class VideoService {

    private final ExerciseRepository exerciseRepository;

    /**
     * Get all uploaded/linked videos
     */
    public VideosResponse getAllVideos() {
        log.info("Fetching all videos");

        List<ExerciseModel> exercises = exerciseRepository.findAll();

        List<VideoDto> videos = exercises.stream()
                .filter(exercise -> exercise.getVideoId() != null && exercise.getVideoUrl() != null)
                .map(exercise -> new VideoDto(exercise.getVideoId(), exercise.getVideoUrl()))
                .distinct()
                .toList();

        return new VideosResponse(videos);
    }
}
