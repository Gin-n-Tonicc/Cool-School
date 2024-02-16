package com.coolSchool.coolSchool.exceptions.course;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class CourseNotFoundException extends ApiException {
    public CourseNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("course.not.found", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }
}