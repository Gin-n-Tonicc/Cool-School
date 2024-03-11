package com.coolSchool.coolSchool.exceptions.rateLimiting;

import com.coolSchool.coolSchool.exceptions.common.TooManyRequestsException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class RateLimitExceededException extends TooManyRequestsException {
    public RateLimitExceededException(MessageSource messageSource) {
        super(messageSource.getMessage("rate.limit.exceeded", null, LocaleContextHolder.getLocale()));
    }
}
