package com.coolSchool.coolSchool.exceptions.userCourse;

import com.coolSchool.coolSchool.exceptions.common.ValidationException;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidationUserCourseException extends ValidationException {
    public ValidationUserCourseException(Set<ConstraintViolation<?>> validationErrors) {
        super(validationErrors);
    }
}
