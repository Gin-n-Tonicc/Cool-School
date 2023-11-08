package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.dto.request.CourseRequestDTO;
import com.coolSchool.CoolSchool.models.dto.response.CourseResponseDTO;

import java.util.List;

public interface CourseService {
    List<CourseResponseDTO> getAllCourses();

    CourseResponseDTO getCourseById(Long id);

    CourseResponseDTO createCourse(CourseRequestDTO courseDTO, PublicUserDTO attribute);

    CourseResponseDTO updateCourse(Long id, CourseRequestDTO courseDTO, PublicUserDTO loggedUser);

    void deleteCourse(Long id, PublicUserDTO attribute);
}
