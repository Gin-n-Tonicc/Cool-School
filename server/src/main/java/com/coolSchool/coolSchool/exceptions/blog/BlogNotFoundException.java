package com.coolSchool.coolSchool.exceptions.blog;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class BlogNotFoundException extends ApiException {
    public BlogNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("blog.not.enabled", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }
}
