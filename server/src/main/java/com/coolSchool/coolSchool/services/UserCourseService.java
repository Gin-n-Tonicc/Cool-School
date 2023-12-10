package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.request.UserCourseRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.UserCourseResponseDTO;

import java.util.List;

public interface UserCourseService {
    List<UserCourseResponseDTO> getAllUserCourses();

    UserCourseResponseDTO getUserCourseById(Long id);

    UserCourseResponseDTO createUserCourse(UserCourseRequestDTO userCourseDTO);

    UserCourseResponseDTO updateUserCourse(Long id, UserCourseRequestDTO userCourseDTO);

    void deleteUserCourse(Long id);
}
