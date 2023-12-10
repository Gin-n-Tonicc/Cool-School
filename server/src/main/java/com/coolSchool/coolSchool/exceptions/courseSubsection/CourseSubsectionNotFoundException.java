package com.coolSchool.coolSchool.exceptions.courseSubsection;

import com.coolSchool.coolSchool.exceptions.common.ApiException;
import org.springframework.http.HttpStatus;

public class CourseSubsectionNotFoundException extends ApiException {
    public CourseSubsectionNotFoundException() {
        super("Course subsection not found", HttpStatus.NOT_FOUND);
    }
}