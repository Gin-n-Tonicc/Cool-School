package com.coolSchool.coolSchool.exceptions.AI;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception thrown when there is an error processing an AI response.
 * Extends BadRequestException to represent a bad request error.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class ErrorProcessingAIResponseException extends BadRequestException {
    public ErrorProcessingAIResponseException(MessageSource messageSource) {
        super(messageSource.getMessage("error.processing.AI.response.exception", null, LocaleContextHolder.getLocale()));
    }
}
