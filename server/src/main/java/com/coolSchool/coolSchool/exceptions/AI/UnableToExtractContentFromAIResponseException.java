package com.coolSchool.coolSchool.exceptions.AI;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception thrown when unable to extract content from an AI response.
 * Extends BadRequestException to represent a bad request error.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class UnableToExtractContentFromAIResponseException extends BadRequestException {
    public UnableToExtractContentFromAIResponseException(MessageSource messageSource) {
        super(messageSource.getMessage("unable.to.extract.content.from.AI.response", null, LocaleContextHolder.getLocale()));
    }
}
