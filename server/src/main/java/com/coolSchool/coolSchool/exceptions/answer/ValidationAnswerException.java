package com.coolSchool.coolSchool.exceptions.answer;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidationAnswerException extends ApiException {
    public ValidationAnswerException(Set<ConstraintViolation<?>> validationErrors) {
        super(
                validationErrors
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining("\n")),
                HttpStatus.BAD_REQUEST
        );
    }
}