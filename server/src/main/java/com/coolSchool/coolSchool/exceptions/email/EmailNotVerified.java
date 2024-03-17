package com.coolSchool.coolSchool.exceptions.email;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception thrown to indicate that the user's email has not been verified.
 * Extends BadRequestException and sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class EmailNotVerified extends BadRequestException {
    public EmailNotVerified(MessageSource messageSource) {
        super(messageSource.getMessage("email.not.verified", null, LocaleContextHolder.getLocale()));
    }
}