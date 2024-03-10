package com.coolSchool.coolSchool.exceptions.user;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import jakarta.validation.ConstraintViolation;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Set;
import java.util.stream.Collectors;

public class UserCreateException extends BadRequestException {
    public UserCreateException(MessageSource messageSource, boolean isUnique) {
        super(
                isUnique
                        ? messageSource.getMessage("user.email.exists", null, LocaleContextHolder.getLocale())
                        : messageSource.getMessage("user.invalid.data", null, LocaleContextHolder.getLocale())
        );
    }

    public UserCreateException(Set<ConstraintViolation<?>> validationErrors) {
        super(
                validationErrors
                        .stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining("\n"))
        );
    }
}
