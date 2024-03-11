package com.coolSchool.coolSchool.exceptions.userCourse;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class UserCourseNotFoundException extends NoSuchElementException {
    public UserCourseNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("user.course.not.found", null, LocaleContextHolder.getLocale()));
    }
}