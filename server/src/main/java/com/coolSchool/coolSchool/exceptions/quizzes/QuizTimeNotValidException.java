package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class QuizTimeNotValidException extends ApiException {
    public QuizTimeNotValidException(MessageSource messageSource) {
        super(messageSource.getMessage("quiz.time.not.valid", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }
}
