package com.coolSchool.coolSchool.exceptions.answer;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception thrown when the answer in a quiz question is not found.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class AnswerNotFoundException extends NoSuchElementException {
    public AnswerNotFoundException(MessageSource messageSource) {

        super(messageSource.getMessage("answer.not.found", null, LocaleContextHolder.getLocale()));
    }
}
