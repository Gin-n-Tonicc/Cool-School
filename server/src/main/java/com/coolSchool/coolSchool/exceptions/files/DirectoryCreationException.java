package com.coolSchool.coolSchool.exceptions.files;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class DirectoryCreationException extends ApiException {
    public DirectoryCreationException(MessageSource messageSource) {
        super(messageSource.getMessage("directory.creation.exception", null, LocaleContextHolder.getLocale()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

