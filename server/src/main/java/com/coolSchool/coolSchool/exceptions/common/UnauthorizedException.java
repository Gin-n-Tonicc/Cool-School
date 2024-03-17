package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown to indicate that the client lacks valid authentication credentials for the requested resource.
 * Extends ApiException and sets the appropriate message and HTTP status code.
 */
public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
