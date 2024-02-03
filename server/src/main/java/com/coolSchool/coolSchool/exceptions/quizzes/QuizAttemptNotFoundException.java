package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class QuizAttemptNotFoundException extends ApiException {
    public QuizAttemptNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("quiz.attempt.not.found", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }
}
