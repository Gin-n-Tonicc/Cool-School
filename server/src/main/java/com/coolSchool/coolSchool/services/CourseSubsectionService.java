package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.request.CourseSubsectionRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CourseSubsectionResponseDTO;

import java.util.List;

public interface CourseSubsectionService {
    List<CourseSubsectionResponseDTO> getAllCourseSubsections();

    List<CourseSubsectionResponseDTO> getAllByCourse(Long id);

    CourseSubsectionResponseDTO getCourseSubsectionById(Long id);

    CourseSubsectionResponseDTO createCourseSubsection(CourseSubsectionRequestDTO courseSubsectionDTO);

    CourseSubsectionResponseDTO updateCourseSubsection(Long id, CourseSubsectionRequestDTO courseSubsectionDTO);

    void deleteCourseSubsection(Long id);
}
