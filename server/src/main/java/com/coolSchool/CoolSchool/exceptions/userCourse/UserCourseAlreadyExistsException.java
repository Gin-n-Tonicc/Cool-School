package com.coolSchool.CoolSchool.exceptions.userCourse;

import com.coolSchool.CoolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class UserCourseAlreadyExistsException extends ApiException {
    public UserCourseAlreadyExistsException() {
        super("This user is already in this course", HttpStatus.BAD_REQUEST);
    }
}
