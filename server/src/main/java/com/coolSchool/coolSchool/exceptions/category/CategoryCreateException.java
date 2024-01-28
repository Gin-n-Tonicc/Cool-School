package com.coolSchool.coolSchool.exceptions.category;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class CategoryCreateException extends ApiException {
    public CategoryCreateException(MessageSource messageSource, boolean isUnique) {
        super(
                isUnique
                        ? messageSource.getMessage("category.create.exists", null, LocaleContextHolder.getLocale())
                        : messageSource.getMessage("category.create.invalid", null, LocaleContextHolder.getLocale()),
                HttpStatus.BAD_REQUEST
        );
    }
}
