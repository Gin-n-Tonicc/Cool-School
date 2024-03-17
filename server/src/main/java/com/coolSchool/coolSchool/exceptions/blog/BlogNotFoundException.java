package com.coolSchool.coolSchool.exceptions.blog;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception thrown when the blog is not found.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class BlogNotFoundException extends NoSuchElementException {
    public BlogNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("blog.not.found", null, LocaleContextHolder.getLocale()));
    }
}
