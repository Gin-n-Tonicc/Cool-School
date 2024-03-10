package com.coolSchool.coolSchool.exceptions.resource;

import com.coolSchool.coolSchool.exceptions.common.ValidationException;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidationResourceException extends ValidationException {
    public ValidationResourceException(Set<ConstraintViolation<?>> validationErrors) {
        super(validationErrors);
    }
}
