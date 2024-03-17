package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.exceptions.course.CourseNotFoundException;
import com.coolSchool.coolSchool.exceptions.user.UserNotFoundException;
import com.coolSchool.coolSchool.exceptions.userCourse.UserCourseAlreadyExistsException;
import com.coolSchool.coolSchool.exceptions.userCourse.UserCourseNotFoundException;
import com.coolSchool.coolSchool.models.dto.request.UserCourseRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.UserCourseResponseDTO;
import com.coolSchool.coolSchool.models.entity.Course;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.models.entity.UserCourse;
import com.coolSchool.coolSchool.repositories.CourseRepository;
import com.coolSchool.coolSchool.repositories.UserCourseRepository;
import com.coolSchool.coolSchool.repositories.UserRepository;
import com.coolSchool.coolSchool.services.UserCourseService;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserCourseServiceImpl implements UserCourseService {
    private final UserCourseRepository userCourseRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    public UserCourseServiceImpl(UserCourseRepository userCourseRepository, UserRepository userRepository, CourseRepository courseRepository, ModelMapper modelMapper, MessageSource messageSource) {
        this.userCourseRepository = userCourseRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
    }

    @Override
    public List<UserCourseResponseDTO> getAllUserCourses() {
        List<UserCourse> userCourses = userCourseRepository.findByDeletedFalse();
        return userCourses.stream().map(userCourse -> modelMapper.map(userCourse, UserCourseResponseDTO.class)).toList();
    }

    @Override
    public UserCourseResponseDTO getUserCourseById(Long id) {
        Optional<UserCourse> userCourse = userCourseRepository.findByIdAndDeletedFalse(id);
        if (userCourse.isPresent()) {
            return modelMapper.map(userCourse.get(), UserCourseResponseDTO.class);
        }
        throw new UserCourseNotFoundException(messageSource);
    }

    @Override
    public UserCourseResponseDTO createUserCourse(UserCourseRequestDTO userCourseDTO) {
        // Check if the relation user-course already exists
        if (userCourseRepository.existsByUserIdAndCourseIdAndDeletedFalse(userCourseDTO.getUserId(), userCourseDTO.getCourseId())) {
            throw new UserCourseAlreadyExistsException(messageSource);
        }
        UserCourse userCourse = new UserCourse();

        User user = userRepository.findByIdAndDeletedFalse(userCourseDTO.getUserId()).orElseThrow(() -> new UserNotFoundException(messageSource));
        Course course = courseRepository.findByIdAndDeletedFalse(userCourseDTO.getCourseId()).orElseThrow(() -> new CourseNotFoundException(messageSource));

        userCourse.setUser(user);
        userCourse.setCourse(course);

        UserCourse userCourseEntity = userCourseRepository.save(userCourse);
        return modelMapper.map(userCourseEntity, UserCourseResponseDTO.class);
    }

    @Override
    public UserCourseResponseDTO updateUserCourse(Long id, UserCourseRequestDTO userCourseDTO) {
        Optional<UserCourse> existingUserCourseOptional = userCourseRepository.findByIdAndDeletedFalse(id);

        if (existingUserCourseOptional.isEmpty()) {
            throw new UserCourseNotFoundException(messageSource);
        }

        UserCourse existingUserCourse = existingUserCourseOptional.get();
        User user = userRepository.findByIdAndDeletedFalse(userCourseDTO.getUserId()).orElseThrow(() -> new UserNotFoundException(messageSource));
        Course course = courseRepository.findByIdAndDeletedFalse(userCourseDTO.getCourseId()).orElseThrow(() -> new CourseNotFoundException(messageSource));

        existingUserCourse.setUser(user);
        existingUserCourse.setCourse(course);

        existingUserCourse.setId(id);
        UserCourse updatedUserCourse = userCourseRepository.save(existingUserCourse);
        return modelMapper.map(updatedUserCourse, UserCourseResponseDTO.class);
    }

    @Override
    public void deleteUserCourse(Long id) {
        Optional<UserCourse> userCourse = userCourseRepository.findByIdAndDeletedFalse(id);
        if (userCourse.isPresent()) {
            // Soft delete
            userCourse.get().setDeleted(true);
            userCourseRepository.save(userCourse.get());
        } else {
            throw new UserCourseNotFoundException(messageSource);
        }
    }
}
