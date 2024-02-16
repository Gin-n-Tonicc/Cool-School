package com.coolSchool.coolSchool.exceptions.AI;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class ErrorProcessingAIResponseException extends BadRequestException {
    public ErrorProcessingAIResponseException(MessageSource messageSource) {
        super(messageSource.getMessage("error.processing.AI.response.exception", null, LocaleContextHolder.getLocale()));
    }
}
