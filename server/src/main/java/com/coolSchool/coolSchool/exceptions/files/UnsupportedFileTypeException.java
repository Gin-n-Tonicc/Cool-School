package com.coolSchool.coolSchool.exceptions.files;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class UnsupportedFileTypeException extends ApiException {
    public UnsupportedFileTypeException(MessageSource messageSource) {
        super(messageSource.getMessage("unsupported.file.type", null, LocaleContextHolder.getLocale()), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
}
