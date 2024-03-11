package com.coolSchool.coolSchool.exceptions.quizzes;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class QuizTimeNotValidException extends BadRequestException {
    public QuizTimeNotValidException(MessageSource messageSource) {
        super(messageSource.getMessage("quiz.time.not.valid", null, LocaleContextHolder.getLocale()));
    }
}
