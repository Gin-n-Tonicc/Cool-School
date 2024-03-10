package com.coolSchool.coolSchool.exceptions.comment;

import com.coolSchool.coolSchool.exceptions.common.ValidationException;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidationCommentException extends ValidationException {
    public ValidationCommentException(Set<ConstraintViolation<?>> validationErrors) {
        super(validationErrors);
    }
}
