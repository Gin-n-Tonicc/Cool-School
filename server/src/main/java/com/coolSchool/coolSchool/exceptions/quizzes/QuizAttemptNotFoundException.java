package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class QuizAttemptNotFoundException extends NoSuchElementException {
    public QuizAttemptNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("quiz.attempt.not.found", null, LocaleContextHolder.getLocale()));
    }
}
