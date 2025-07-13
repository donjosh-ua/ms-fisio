package com.ms_fisio.exception.model;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now();
    private int statusCode;
    private String statusName;
    private String message;
    private Object data;

    @Builder
    public ErrorResponse(HttpStatus httpStatus, Exception ex, String message, Object data) {
        this.statusCode = httpStatus.value();
        this.statusName = httpStatus.name();
        this.message = (message != null) ? message : ex.getMessage();
        this.data = data;
    }

}
