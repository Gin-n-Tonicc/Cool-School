package com.coolSchool.CoolSchool.exceptions.user;

import com.coolSchool.CoolSchool.exceptions.ApiException;
import com.coolSchool.CoolSchool.models.entity.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.http.HttpStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserCreateException extends ApiException {
    public UserCreateException(boolean isUnique) {
        super(
                isUnique
                        ? "User with the same email already exists"
                        : "Invalid user data",
                HttpStatus.NOT_FOUND
        );
    }

    public UserCreateException(Set<ConstraintViolation<?>> validationErrors) {
        super(
                validationErrors
                .stream()
                .map(x -> x.getPropertyPath() + " " + x.getMessage())
                .collect(Collectors.joining("\n")),
                HttpStatus.NOT_FOUND
        );
    }
}
