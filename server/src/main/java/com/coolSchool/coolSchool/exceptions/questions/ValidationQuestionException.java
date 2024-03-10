package com.coolSchool.coolSchool.exceptions.questions;

import com.coolSchool.coolSchool.exceptions.common.ValidationException;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidationQuestionException extends ValidationException {
    public ValidationQuestionException(Set<ConstraintViolation<?>> validationErrors) {
        super(validationErrors);
    }
}
