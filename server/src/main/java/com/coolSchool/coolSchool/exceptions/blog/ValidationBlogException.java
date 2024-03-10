package com.coolSchool.coolSchool.exceptions.blog;

import com.coolSchool.coolSchool.exceptions.common.ValidationException;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

public class ValidationBlogException extends ValidationException {
    public ValidationBlogException(Set<ConstraintViolation<?>> validationErrors) {
        super(validationErrors);
    }
}
