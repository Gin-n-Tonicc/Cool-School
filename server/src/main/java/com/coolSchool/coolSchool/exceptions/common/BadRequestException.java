package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ApiException {
    public BadRequestException(MessageSource messageSource) {
        super(messageSource.getMessage("bad.request", null, LocaleContextHolder.getLocale()), HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
