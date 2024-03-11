package com.coolSchool.coolSchool.exceptions.review;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class ReviewNotFoundException extends NoSuchElementException {
    public ReviewNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("review.not.found", null, LocaleContextHolder.getLocale()));
    }
}