package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.request.CourseSubsectionRequestDTO;
import com.coolSchool.CoolSchool.models.dto.response.CourseSubsectionResponseDTO;

import java.util.List;

public interface CourseSubsectionService {
    List<CourseSubsectionResponseDTO> getAllCourseSubsections();
    List<CourseSubsectionResponseDTO> getAllByCourse(Long id);

    CourseSubsectionResponseDTO getCourseSubsectionById(Long id);

    CourseSubsectionResponseDTO createCourseSubsection(CourseSubsectionRequestDTO courseSubsectionDTO);

    CourseSubsectionResponseDTO updateCourseSubsection(Long id, CourseSubsectionRequestDTO courseSubsectionDTO);

    void deleteCourseSubsection(Long id);
}
