package com.coolSchool.coolSchool.exceptions.email;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class EmailNotVerified extends BadRequestException {
    public EmailNotVerified(MessageSource messageSource) {
        super(messageSource.getMessage("email.not.verified", null, LocaleContextHolder.getLocale()));
    }
}