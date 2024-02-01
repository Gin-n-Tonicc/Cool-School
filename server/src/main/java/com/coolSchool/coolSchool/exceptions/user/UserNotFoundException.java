package com.coolSchool.coolSchool.exceptions.user;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ApiException {
    public UserNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }

    public UserNotFoundException(String field, MessageSource messageSource) {
        super(messageSource.getMessage("user.not.found", null, LocaleContextHolder.getLocale()) + field, HttpStatus.NOT_FOUND);
    }
}
