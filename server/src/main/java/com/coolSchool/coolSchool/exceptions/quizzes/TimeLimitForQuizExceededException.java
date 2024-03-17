package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception thrown when the time limit for completing the quiz has been exceeded.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class TimeLimitForQuizExceededException extends BadRequestException {
    public TimeLimitForQuizExceededException(MessageSource messageSource) {
        super(messageSource.getMessage("time.limit.exceeded.quiz", null, LocaleContextHolder.getLocale()));
    }
}
