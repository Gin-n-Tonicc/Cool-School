package com.coolSchool.coolSchool.exceptions.course;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class CourseNotFoundException extends NoSuchElementException {
    public CourseNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("course.not.found", null, LocaleContextHolder.getLocale()));
    }
}