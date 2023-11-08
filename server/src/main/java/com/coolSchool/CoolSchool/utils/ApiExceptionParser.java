package com.coolSchool.CoolSchool.utils;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import com.coolSchool.CoolSchool.models.dto.response.ExceptionResponse;

import java.time.LocalDateTime;

public class ApiExceptionParser {
    public static ExceptionResponse parseException(ApiException exception) {
        return ExceptionResponse
                .builder()
                .dateTime(LocalDateTime.now())
                .message(exception.getMessage())
                .status(exception.getStatus())
                .statusCode(exception.getStatusCode())
                .build();
    }
}
