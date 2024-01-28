package com.coolSchool.coolSchool.exceptions.user;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class UserLoginException extends ApiException {
    public UserLoginException(MessageSource messageSource) {
        super(messageSource.getMessage("user.login.exception", null, LocaleContextHolder.getLocale()), HttpStatus.BAD_REQUEST);
    }
}
