package com.ms_fisio.video.controller;

import com.ms_fisio.video.dto.VideosResponse;
import com.ms_fisio.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for video endpoints
 */
@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
@Slf4j
public class VideoController {

    private final VideoService videoService;

    /**
     * Get all uploaded/linked videos
     */
    @GetMapping
    public ResponseEntity<VideosResponse> getAllVideos() {
        log.info("Request to get all videos");

        VideosResponse response = videoService.getAllVideos();
        return ResponseEntity.ok(response);
    }
}
