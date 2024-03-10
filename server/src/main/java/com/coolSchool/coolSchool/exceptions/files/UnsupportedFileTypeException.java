package com.coolSchool.coolSchool.exceptions.files;

import com.coolSchool.coolSchool.exceptions.common.UnsupportedMediaTypeException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class UnsupportedFileTypeException extends UnsupportedMediaTypeException {
    public UnsupportedFileTypeException(MessageSource messageSource) {
        super(messageSource.getMessage("unsupported.file.type", null, LocaleContextHolder.getLocale()));
    }
}
