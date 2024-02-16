package com.coolSchool.coolSchool.exceptions.resource;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("resource.not.found", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }
}