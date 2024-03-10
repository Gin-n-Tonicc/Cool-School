package com.coolSchool.coolSchool.exceptions.user;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class UserLoginException extends BadRequestException {
    public UserLoginException(MessageSource messageSource) {
        super(messageSource.getMessage("user.login.exception", null, LocaleContextHolder.getLocale()));
    }
}
