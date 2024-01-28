package com.coolSchool.coolSchool.exceptions.blog;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class BlogAlreadyLikedException extends BadRequestException {
    public BlogAlreadyLikedException(MessageSource messageSource) {
        super(messageSource.getMessage("blog.already.liked", null, LocaleContextHolder.getLocale()));
    }
}
