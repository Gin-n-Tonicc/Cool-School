package com.coolSchool.coolSchool.exceptions.token;

import com.coolSchool.coolSchool.exceptions.common.UnauthorizedException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class InvalidTokenException extends UnauthorizedException {
    public InvalidTokenException(MessageSource messageSource) {
        super(messageSource.getMessage("token.invalid", null, LocaleContextHolder.getLocale()));
    }
}
