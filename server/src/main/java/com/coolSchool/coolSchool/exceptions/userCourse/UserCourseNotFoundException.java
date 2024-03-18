package com.coolSchool.coolSchool.exceptions.userCourse;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Exception thrown when the relation between user course is not found.
 * Sets the appropriate message using MessageSource (the messages are in src/main/resources/messages).
 */
public class UserCourseNotFoundException extends NoSuchElementException {
    public UserCourseNotFoundException(MessageSource messageSource) {
        super(messageSource.getMessage("user.course.not.found", null, LocaleContextHolder.getLocale()));
    }
}