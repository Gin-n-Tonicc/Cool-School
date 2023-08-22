package com.coolSchool.CoolSchool.exceptions.common;

import lombok.Getter;
import org.springframework.http.HttpStatus;

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
