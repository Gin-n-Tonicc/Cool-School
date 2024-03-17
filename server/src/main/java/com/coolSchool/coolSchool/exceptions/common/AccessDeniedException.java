package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown to indicate access denied errors.
 * Extends ApiException and sets the appropriate message and HTTP status code.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class AccessDeniedException extends ApiException {
    public AccessDeniedException(MessageSource messageSource) {
        super(messageSource.getMessage("access.denied", null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
    }
}
