package com.coolSchool.coolSchool.exceptions.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Abstract base class for API exceptions.
 * Extends RuntimeException to indicate unchecked exceptions that can occur during API operations.
 */
@Getter
public abstract class ApiException extends RuntimeException {
    private HttpStatus status;
    private Integer statusCode;

    protected ApiException(String message, HttpStatus status) {
        super(message);
        setStatus(status);
        setStatusCode(status.value());
    }

    private void setStatus(HttpStatus status) {
        this.status = status;
    }

    private void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
