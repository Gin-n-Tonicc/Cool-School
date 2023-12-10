package com.coolSchool.coolSchool.exceptions.course;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class CourseNotFoundException extends ApiException {
    public CourseNotFoundException() {
        super("Course not found", HttpStatus.NOT_FOUND);
    }
}