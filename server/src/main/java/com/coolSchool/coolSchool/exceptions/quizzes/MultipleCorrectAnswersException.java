package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception indicating that multiple correct answers were provided where only one was expected.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class MultipleCorrectAnswersException extends BadRequestException {
    public MultipleCorrectAnswersException(MessageSource messageSource) {
        super(messageSource.getMessage("no.multiple.correct.answers", null, LocaleContextHolder.getLocale()));
    }
}
