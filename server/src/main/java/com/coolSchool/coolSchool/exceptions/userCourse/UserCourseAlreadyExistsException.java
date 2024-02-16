package com.coolSchool.coolSchool.exceptions.userCourse;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

public class UserCourseAlreadyExistsException extends ApiException {
    public UserCourseAlreadyExistsException(MessageSource messageSource) {
        super(messageSource.getMessage("user.course.already.exists", null, LocaleContextHolder.getLocale()), HttpStatus.BAD_REQUEST);
    }
}
