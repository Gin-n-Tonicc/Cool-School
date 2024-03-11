package com.coolSchool.coolSchool.exceptions.files;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class FileNotFoundException extends NoSuchElementException {
    public FileNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("file.not.found", null, LocaleContextHolder.getLocale()));
    }
}
