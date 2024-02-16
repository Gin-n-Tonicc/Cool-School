package com.coolSchool.coolSchool.exceptions.answer;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class AnswerNotFoundException extends ApiException {
    public AnswerNotFoundException(MessageSource messageSource) {

        super(messageSource.getMessage("answer.not.found", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }
}
