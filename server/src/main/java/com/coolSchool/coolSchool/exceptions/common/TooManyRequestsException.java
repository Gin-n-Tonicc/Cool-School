package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown to indicate that the client has sent too many requests in a given amount of time.
 * Related to @RateLimited()
 * Extends ApiException and sets the appropriate message and HTTP status code.
 */
public class TooManyRequestsException extends ApiException {
    public TooManyRequestsException(String message) {
        super(message, HttpStatus.TOO_MANY_REQUESTS);
    }
}
