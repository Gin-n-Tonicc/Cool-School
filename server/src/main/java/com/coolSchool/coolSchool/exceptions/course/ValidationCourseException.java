package com.coolSchool.coolSchool.exceptions.course;

import com.coolSchool.coolSchool.exceptions.common.ValidationException;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidationCourseException extends ValidationException {
    public ValidationCourseException(Set<ConstraintViolation<?>> validationErrors) {
        super(validationErrors);
    }
}
