package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception thrown when the current time is not within the valid time range for the quiz.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class QuizTimeNotValidException extends BadRequestException {
    public QuizTimeNotValidException(MessageSource messageSource) {
        super(messageSource.getMessage("quiz.time.not.valid", null, LocaleContextHolder.getLocale()));
    }
}
