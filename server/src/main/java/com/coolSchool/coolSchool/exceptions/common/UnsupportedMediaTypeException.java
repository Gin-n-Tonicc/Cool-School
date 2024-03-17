package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown to indicate that the server refuses to accept the request because the payload format is
 * in an unsupported format.
 * Extends ApiException and sets the appropriate message and HTTP status code.
 */
public class UnsupportedMediaTypeException extends ApiException {
    public UnsupportedMediaTypeException(String message) {
        super(message, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}