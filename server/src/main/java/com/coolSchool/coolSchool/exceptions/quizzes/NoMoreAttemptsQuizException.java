package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class NoMoreAttemptsQuizException extends BadRequestException {
    public NoMoreAttemptsQuizException(MessageSource messageSource) {
        super(messageSource.getMessage("quiz.no.more.attempts", null, LocaleContextHolder.getLocale()));
    }
}
