package com.coolSchool.coolSchool.exceptions.common;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class AccessDeniedException extends ApiException {
    public AccessDeniedException(MessageSource messageSource) {
        super(messageSource.getMessage("access.denied", null, LocaleContextHolder.getLocale()), HttpStatus.FORBIDDEN);
    }
}
