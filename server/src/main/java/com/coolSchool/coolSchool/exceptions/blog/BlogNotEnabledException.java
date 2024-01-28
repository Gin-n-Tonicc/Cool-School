package com.coolSchool.coolSchool.exceptions.blog;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class BlogNotEnabledException extends ApiException {
    public BlogNotEnabledException(MessageSource messageSource) {
        super(messageSource.getMessage("blog.not.found", null, LocaleContextHolder.getLocale()), HttpStatus.BAD_REQUEST);
    }
}
