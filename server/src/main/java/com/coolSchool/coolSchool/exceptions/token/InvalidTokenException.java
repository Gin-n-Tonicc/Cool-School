package com.coolSchool.coolSchool.exceptions.token;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends ApiException {
    public InvalidTokenException(MessageSource messageSource) {
        super(messageSource.getMessage("token.invalid", null, LocaleContextHolder.getLocale()), HttpStatus.UNAUTHORIZED);
    }
}
