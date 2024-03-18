package com.coolSchool.coolSchool.exceptions.files;

import com.coolSchool.coolSchool.exceptions.common.InternalServerErrorException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception thrown to indicate an error during directory creation.
 * Extends InternalServerErrorException and sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class DirectoryCreationException extends InternalServerErrorException {
    public DirectoryCreationException(MessageSource messageSource) {
        super(messageSource.getMessage("directory.creation.exception", null, LocaleContextHolder.getLocale()));
    }
}

