package com.coolSchool.coolSchool.exceptions.files;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class FileNotFoundException extends ApiException {
    public FileNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("file.not.found", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }
}
