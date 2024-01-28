package com.coolSchool.coolSchool.exceptions.userCourse;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class UserCourseNotFoundException extends ApiException {
    public UserCourseNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("user.course.not.found", null, LocaleContextHolder.getLocale()), HttpStatus.NOT_FOUND);
    }
}