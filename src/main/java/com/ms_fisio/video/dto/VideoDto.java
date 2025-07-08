package com.ms_fisio.video.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for video information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VideoDto {
    
    @JsonProperty("video_id")
    private String videoId;
    private String url;
}
