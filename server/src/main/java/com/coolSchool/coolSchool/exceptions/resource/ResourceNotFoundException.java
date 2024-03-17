package com.coolSchool.coolSchool.exceptions.resource;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception indicating that the resource is not found.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class ResourceNotFoundException extends NoSuchElementException {
    public ResourceNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("resource.not.found", null, LocaleContextHolder.getLocale()));
    }
}