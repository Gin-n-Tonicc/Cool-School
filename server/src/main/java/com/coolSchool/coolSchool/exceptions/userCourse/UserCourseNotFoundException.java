package com.coolSchool.coolSchool.exceptions.userCourse;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class UserCourseNotFoundException extends ApiException {
    public UserCourseNotFoundException() {
        super("UserCourse not found", HttpStatus.NOT_FOUND);
    }
}