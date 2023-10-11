package com.coolSchool.CoolSchool.exceptions.courseSubsection;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidationCourseSubsectionException extends ApiException {
    public ValidationCourseSubsectionException(Set<ConstraintViolation<?>> validationErrors) {
        super(
                validationErrors
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining("\n")),
                HttpStatus.BAD_REQUEST
        );
    }
}
