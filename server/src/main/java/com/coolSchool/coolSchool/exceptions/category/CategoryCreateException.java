package com.coolSchool.coolSchool.exceptions.category;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception thrown when a category with the same name already exists.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class CategoryCreateException extends BadRequestException {
    public CategoryCreateException(MessageSource messageSource, boolean isUnique) {
        super(
                isUnique
                        ? messageSource.getMessage("category.create.exists", null, LocaleContextHolder.getLocale())
                        : messageSource.getMessage("category.create.invalid", null, LocaleContextHolder.getLocale())
        );
    }
}
