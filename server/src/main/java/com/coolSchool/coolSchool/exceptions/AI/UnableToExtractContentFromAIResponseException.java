package com.coolSchool.coolSchool.exceptions.AI;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class UnableToExtractContentFromAIResponseException extends BadRequestException {
    public UnableToExtractContentFromAIResponseException(MessageSource messageSource) {
        super(messageSource.getMessage("unable.to.extract.content.from.AI.response", null, LocaleContextHolder.getLocale()));
    }
}
