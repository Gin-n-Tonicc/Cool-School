package com.coolSchool.CoolSchool.exceptions.user;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;

import java.util.Set;
import java.util.stream.Collectors;

public class UserCreateException extends ApiException {
    public UserCreateException(boolean isUnique) {
        super(
                isUnique
                        ? "User with the same email already exists"
                        : "Invalid user data",
                HttpStatus.BAD_REQUEST
        );
    }

    public UserCreateException(Set<ConstraintViolation<?>> validationErrors) {
        super(
                validationErrors
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining("\n")),
                HttpStatus.BAD_REQUEST
        );
    }
}
