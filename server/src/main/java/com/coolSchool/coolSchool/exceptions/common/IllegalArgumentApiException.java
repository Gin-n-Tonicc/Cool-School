package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class IllegalArgumentApiException extends ApiException {

    public IllegalArgumentApiException(MessageSource messageSource) {
        super(messageSource.getMessage("illegal.argument", null, LocaleContextHolder.getLocale()), HttpStatus.BAD_REQUEST);
    }
}
