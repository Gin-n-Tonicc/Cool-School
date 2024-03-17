package com.coolSchool.coolSchool.exceptions.review;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception indicating that the review is not found.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class ReviewNotFoundException extends NoSuchElementException {
    public ReviewNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("review.not.found", null, LocaleContextHolder.getLocale()));
    }
}