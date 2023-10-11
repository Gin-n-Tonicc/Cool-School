package com.coolSchool.CoolSchool.exceptions.userCourse;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class UserCourseNotFoundException extends ApiException {
    public UserCourseNotFoundException() {
        super("UserCourse not found", HttpStatus.NOT_FOUND);
    }
}