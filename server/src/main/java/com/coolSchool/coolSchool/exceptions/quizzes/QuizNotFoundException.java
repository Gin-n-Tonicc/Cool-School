package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class QuizNotFoundException extends ApiException {
    public QuizNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("quiz.not.found", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }
}