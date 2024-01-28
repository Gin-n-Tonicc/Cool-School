package com.coolSchool.coolSchool.exceptions.comment;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class CommentNotFoundException extends ApiException {
    public CommentNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("comment.not.found", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }
}
