package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.CourseSubsectionDTO;

import java.util.List;

public interface CourseSubsectionService {
    List<CourseSubsectionDTO> getAllCourseSubsections();

    CourseSubsectionDTO getCourseSubsectionById(Long id);

    CourseSubsectionDTO createCourseSubsection(CourseSubsectionDTO courseSubsectionDTO);

    CourseSubsectionDTO updateCourseSubsection(Long id, CourseSubsectionDTO courseSubsectionDTO);

    void deleteCourseSubsection(Long id);
}
