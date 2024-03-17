package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

/**
 * Exception thrown to indicate that a requested element could not be found.
 * Extends ApiException and sets the appropriate message and HTTP status code.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class NoSuchElementException extends ApiException {
    public NoSuchElementException(MessageSource messageSource) {
        super(messageSource.getMessage("no.such.element", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }

    public NoSuchElementException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
