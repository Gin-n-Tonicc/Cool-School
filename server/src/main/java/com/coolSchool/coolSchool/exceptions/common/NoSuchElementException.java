package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class NoSuchElementException extends ApiException {
    public NoSuchElementException(MessageSource messageSource) {
        super(messageSource.getMessage("no.such.element", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }

    public NoSuchElementException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
