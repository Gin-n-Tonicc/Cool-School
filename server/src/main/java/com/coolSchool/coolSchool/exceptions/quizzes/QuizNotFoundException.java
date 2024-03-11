package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class QuizNotFoundException extends NoSuchElementException {
    public QuizNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("quiz.not.found", null, LocaleContextHolder.getLocale()));
    }
}