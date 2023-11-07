package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.CourseDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;

import java.util.List;

public interface CourseService {
    List<CourseDTO> getAllCourses();

    CourseDTO getCourseById(Long id);

    CourseDTO createCourse(CourseDTO courseDTO, PublicUserDTO attribute);

    CourseDTO updateCourse(Long id, CourseDTO courseDTO, PublicUserDTO loggedUser);

    void deleteCourse(Long id, PublicUserDTO attribute);
}
