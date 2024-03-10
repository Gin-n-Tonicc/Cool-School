package com.coolSchool.coolSchool.exceptions.userCourse;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class UserCourseAlreadyExistsException extends BadRequestException {
    public UserCourseAlreadyExistsException(MessageSource messageSource) {
        super(messageSource.getMessage("user.course.already.exists", null, LocaleContextHolder.getLocale()));
    }
}
