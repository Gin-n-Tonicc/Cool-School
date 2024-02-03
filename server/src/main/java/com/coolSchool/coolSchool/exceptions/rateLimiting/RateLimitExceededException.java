package com.coolSchool.coolSchool.exceptions.rateLimiting;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class RateLimitExceededException extends ApiException {
    public RateLimitExceededException(MessageSource messageSource) {
        super(messageSource.getMessage("rate.limit.exceeded", null, LocaleContextHolder.getLocale()), HttpStatus.TOO_MANY_REQUESTS);
    }
}
