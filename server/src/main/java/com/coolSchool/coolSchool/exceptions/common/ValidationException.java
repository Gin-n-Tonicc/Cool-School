package com.coolSchool.coolSchool.exceptions.common;


import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Exception thrown to indicate that a validation error has occurred.
 * Error messages are based on the one in the entities.
 * Extends ApiException and sets the appropriate message and HTTP status code.
 */
public class ValidationException extends ApiException {
    public ValidationException(Set<ConstraintViolation<?>> validationErrors) {
        super(
                validationErrors
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining("\n")),
                HttpStatus.BAD_REQUEST
        );
    }
}
