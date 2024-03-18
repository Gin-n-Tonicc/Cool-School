package com.coolSchool.coolSchool.exceptions.userCourse;

import com.coolSchool.coolSchool.exceptions.common.BadRequestException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception thrown when attempting to create a user course that already exists.
 * The user is already enrolled in the course.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class UserCourseAlreadyExistsException extends BadRequestException {
    public UserCourseAlreadyExistsException(MessageSource messageSource) {
        super(messageSource.getMessage("user.course.already.exists", null, LocaleContextHolder.getLocale()));
    }
}
