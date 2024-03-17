package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception indicating that the attempt to a quiz is not found.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class QuizAttemptNotFoundException extends NoSuchElementException {
    public QuizAttemptNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("quiz.attempt.not.found", null, LocaleContextHolder.getLocale()));
    }
}
