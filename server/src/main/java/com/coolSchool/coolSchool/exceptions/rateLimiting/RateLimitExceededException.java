package com.coolSchool.coolSchool.exceptions.rateLimiting;

import com.coolSchool.coolSchool.exceptions.common.TooManyRequestsException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * It indicates that the user has made too many requests within a specified time frame,
 * and hence further requests are not allowed until the rate limit resets.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class RateLimitExceededException extends TooManyRequestsException {
    public RateLimitExceededException(MessageSource messageSource) {
        super(messageSource.getMessage("rate.limit.exceeded", null, LocaleContextHolder.getLocale()));
    }
}
