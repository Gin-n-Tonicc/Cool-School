package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.CourseRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CourseResponseDTO;

import java.util.List;

public interface CourseService {
    List<CourseResponseDTO> getAllCourses();

    CourseResponseDTO getCourseById(Long id);

    boolean canEnrollCourse(Long id, PublicUserDTO loggedUser);

    void enrollCourse(Long id, PublicUserDTO loggedUser);

    CourseResponseDTO createCourse(CourseRequestDTO courseDTO, PublicUserDTO attribute);

    CourseResponseDTO updateCourse(Long id, CourseRequestDTO courseDTO, PublicUserDTO loggedUser);

    void deleteCourse(Long id, PublicUserDTO attribute);
}
