package com.coolSchool.coolSchool.exceptions.blog;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class BlogNotEnabledException extends BadRequestException {
    public BlogNotEnabledException(MessageSource messageSource) {
        super(messageSource.getMessage("blog.not.found", null, LocaleContextHolder.getLocale()));
    }
}
