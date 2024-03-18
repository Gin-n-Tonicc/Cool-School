package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown to indicate bad request errors.
 * Extends ApiException and sets the appropriate message and HTTP status code.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class BadRequestException extends ApiException {
    public BadRequestException(MessageSource messageSource) {
        super(messageSource.getMessage("bad.request", null, LocaleContextHolder.getLocale()), HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
