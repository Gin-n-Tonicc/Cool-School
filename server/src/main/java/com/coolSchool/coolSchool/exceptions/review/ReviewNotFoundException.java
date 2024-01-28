package com.coolSchool.coolSchool.exceptions.review;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class ReviewNotFoundException extends ApiException {
    public ReviewNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("review.not.found", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }
}