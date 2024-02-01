package com.coolSchool.coolSchool.exceptions.courseSubsection;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class CourseSubsectionNotFoundException extends ApiException {
    public CourseSubsectionNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("course.subsection.not.found", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }
}