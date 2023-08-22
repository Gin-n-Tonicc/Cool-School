package com.coolSchool.CoolSchool.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class ExceptionResponse {
    private String message;
    private LocalDateTime dateTime;
    private HttpStatus status;
    private Integer statusCode;
}