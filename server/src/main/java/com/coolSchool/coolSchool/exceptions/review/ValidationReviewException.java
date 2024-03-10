package com.coolSchool.coolSchool.exceptions.review;

import com.coolSchool.coolSchool.exceptions.common.ValidationException;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidationReviewException extends ValidationException {
    public ValidationReviewException(Set<ConstraintViolation<?>> validationErrors) {
        super(validationErrors);
    }
}
