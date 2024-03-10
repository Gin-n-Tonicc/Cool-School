package com.coolSchool.coolSchool.exceptions.category;

import com.coolSchool.coolSchool.exceptions.common.ValidationException;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidationCategoryException extends ValidationException {
    public ValidationCategoryException(Set<ConstraintViolation<?>> validationErrors) {
        super(validationErrors);
    }
}
