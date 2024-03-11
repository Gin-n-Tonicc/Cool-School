package com.coolSchool.coolSchool.exceptions.courseSubsection;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class CourseSubsectionNotFoundException extends NoSuchElementException {
    public CourseSubsectionNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("course.subsection.not.found", null, LocaleContextHolder.getLocale()));
    }
}