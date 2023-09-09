package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.UserCourseDTO;

import java.util.List;

public interface UserCourseService {
    List<UserCourseDTO> getAllUserCourses();

    UserCourseDTO getUserCourseById(Long id);

    UserCourseDTO createUserCourse(UserCourseDTO userCourseDTO);

    UserCourseDTO updateUserCourse(Long id, UserCourseDTO userCourseDTO);

    void deleteUserCourse(Long id);
}
