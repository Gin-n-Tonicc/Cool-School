package com.coolSchool.coolSchool.exceptions.userCourse;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class UserCourseAlreadyExistsException extends ApiException {
    public UserCourseAlreadyExistsException() {
        super("This user is already in this course", HttpStatus.BAD_REQUEST);
    }
}
