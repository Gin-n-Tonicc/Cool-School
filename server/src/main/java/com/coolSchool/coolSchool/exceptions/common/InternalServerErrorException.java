package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ApiException {
    public InternalServerErrorException(MessageSource messageSource) {
        super(messageSource.getMessage("internal.server.error", null, LocaleContextHolder.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    public InternalServerErrorException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
