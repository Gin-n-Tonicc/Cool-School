package com.coolSchool.coolSchool.exceptions.email;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class EmailNotVerified extends ApiException {
    public EmailNotVerified(MessageSource messageSource) {
        super(messageSource.getMessage("email.not.verified", null, LocaleContextHolder.getLocale()), HttpStatus.BAD_REQUEST);
    }
}