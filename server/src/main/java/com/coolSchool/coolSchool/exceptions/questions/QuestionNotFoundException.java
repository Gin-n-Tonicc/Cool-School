package com.coolSchool.coolSchool.exceptions.questions;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class QuestionNotFoundException extends ApiException {
    public QuestionNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("question.not.found", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }
}