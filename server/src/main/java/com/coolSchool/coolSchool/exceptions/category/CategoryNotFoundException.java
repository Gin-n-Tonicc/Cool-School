package com.coolSchool.coolSchool.exceptions.category;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class CategoryNotFoundException extends NoSuchElementException {
    public CategoryNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("category.not.found", null, LocaleContextHolder.getLocale()));
    }
}