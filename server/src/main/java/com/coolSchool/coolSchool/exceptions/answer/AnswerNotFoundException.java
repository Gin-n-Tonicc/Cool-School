package com.coolSchool.coolSchool.exceptions.answer;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class AnswerNotFoundException extends NoSuchElementException {
    public AnswerNotFoundException(MessageSource messageSource) {

        super(messageSource.getMessage("answer.not.found", null, LocaleContextHolder.getLocale()));
    }
}
