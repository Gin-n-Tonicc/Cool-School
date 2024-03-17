package com.coolSchool.coolSchool.exceptions.user;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import jakarta.validation.ConstraintViolation;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Exception thrown when there is an issue creating a user, either due to invalid data or duplicate user details.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class UserCreateException extends BadRequestException {

    /**
     * Constructs a UserCreateException with a message indicating either a duplicate email or invalid user data.
     */
    public UserCreateException(MessageSource messageSource, boolean isUnique) {
        super(
                isUnique
                        ? messageSource.getMessage("user.email.exists", null, LocaleContextHolder.getLocale())
                        : messageSource.getMessage("user.invalid.data", null, LocaleContextHolder.getLocale())
        );
    }

    /**
     * Constructs a UserCreateException with validation errors.
     */
    public UserCreateException(Set<ConstraintViolation<?>> validationErrors) {
        super(
                validationErrors
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining("\n"))
        );
    }
}
