package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.ValidationException;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidationQuizException extends ValidationException {
    public ValidationQuizException(Set<ConstraintViolation<?>> validationErrors) {
        super(validationErrors);
    }
}
