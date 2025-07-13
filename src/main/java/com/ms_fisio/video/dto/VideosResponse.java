package com.ms_fisio.video.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for videos response
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideosResponse {

    private List<VideoDto> videos;
}
