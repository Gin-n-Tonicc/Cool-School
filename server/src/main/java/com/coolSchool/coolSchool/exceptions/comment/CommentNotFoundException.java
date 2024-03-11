package com.coolSchool.coolSchool.exceptions.comment;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class CommentNotFoundException extends NoSuchElementException {
    public CommentNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("comment.not.found", null, LocaleContextHolder.getLocale()));
    }
}
