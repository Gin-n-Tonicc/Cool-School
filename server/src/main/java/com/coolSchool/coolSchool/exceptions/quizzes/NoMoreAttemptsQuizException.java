package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception indicating that there are no more attempts available for a quiz.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class NoMoreAttemptsQuizException extends BadRequestException {
    public NoMoreAttemptsQuizException(MessageSource messageSource) {
        super(messageSource.getMessage("quiz.no.more.attempts", null, LocaleContextHolder.getLocale()));
    }
}
