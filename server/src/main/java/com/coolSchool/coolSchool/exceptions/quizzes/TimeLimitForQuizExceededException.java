package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;


public class TimeLimitForQuizExceededException extends ApiException {
    public TimeLimitForQuizExceededException(MessageSource messageSource) {
        super(messageSource.getMessage("time.limit.exceeded.quiz", null, LocaleContextHolder.getLocale()), HttpStatus.BAD_REQUEST);
    }
}
