package com.coolSchool.coolSchool.exceptions.courseSubsection;

import com.coolSchool.coolSchool.exceptions.common.ValidationException;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidationCourseSubsectionException extends ValidationException {
    public ValidationCourseSubsectionException(Set<ConstraintViolation<?>> validationErrors) {
        super(validationErrors);
    }
}
