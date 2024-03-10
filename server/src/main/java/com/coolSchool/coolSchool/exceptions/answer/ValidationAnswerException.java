package com.coolSchool.coolSchool.exceptions.answer;

import com.coolSchool.coolSchool.exceptions.common.ValidationException;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidationAnswerException extends ValidationException {
    public ValidationAnswerException(Set<ConstraintViolation<?>> validationErrors) {
        super(validationErrors);
    }
}
