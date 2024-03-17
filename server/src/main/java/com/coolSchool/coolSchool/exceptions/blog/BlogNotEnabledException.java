package com.coolSchool.coolSchool.exceptions.blog;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * An exception is thrown when a user tries to access a blog that has not yet been enabled by the ADMIN.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class BlogNotEnabledException extends BadRequestException {
    public BlogNotEnabledException(MessageSource messageSource) {
        super(messageSource.getMessage("blog.not.found", null, LocaleContextHolder.getLocale()));
    }
}
