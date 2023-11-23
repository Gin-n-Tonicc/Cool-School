package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.exceptions.course.CourseNotFoundException;
import com.coolSchool.CoolSchool.exceptions.user.UserNotFoundException;
import com.coolSchool.CoolSchool.exceptions.userCourse.UserCourseAlreadyExistsException;
import com.coolSchool.CoolSchool.exceptions.userCourse.UserCourseNotFoundException;
import com.coolSchool.CoolSchool.exceptions.userCourse.ValidationUserCourseException;
import com.coolSchool.CoolSchool.models.dto.request.UserCourseRequestDTO;
import com.coolSchool.CoolSchool.models.dto.response.UserCourseResponseDTO;
import com.coolSchool.CoolSchool.models.entity.Course;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.models.entity.UserCourse;
import com.coolSchool.CoolSchool.repositories.CourseRepository;
import com.coolSchool.CoolSchool.repositories.UserCourseRepository;
import com.coolSchool.CoolSchool.repositories.UserRepository;
import com.coolSchool.CoolSchool.services.UserCourseService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.util.List;
import java.util.Optional;

@Service
public class UserCourseServiceImpl implements UserCourseService {
    private final UserCourseRepository userCourseRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;

    public UserCourseServiceImpl(UserCourseRepository userCourseRepository, UserRepository userRepository, CourseRepository courseRepository, ModelMapper modelMapper, Validator validator) {
        this.userCourseRepository = userCourseRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
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
        throw new UserCourseNotFoundException();
    }

    @Override
    public UserCourseResponseDTO createUserCourse(UserCourseRequestDTO userCourseDTO) {
        try {
            if (userCourseRepository.existsByUserIdAndCourseIdAndDeletedFalse(userCourseDTO.getUserId(), userCourseDTO.getCourseId())) {
                throw new UserCourseAlreadyExistsException();
            }
            UserCourse userCourse = new UserCourse();

            User user = userRepository.findByIdAndDeletedFalse(userCourseDTO.getUserId()).orElseThrow(UserNotFoundException::new);
            Course course = courseRepository.findByIdAndDeletedFalse(userCourseDTO.getCourseId()).orElseThrow(CourseNotFoundException::new);

            userCourse.setUser(user);
            userCourse.setCourse(course);

            UserCourse userCourseEntity = userCourseRepository.save(userCourse);
            return modelMapper.map(userCourseEntity, UserCourseResponseDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationUserCourseException(exception.getConstraintViolations());
        }
    }

    @Override
    public UserCourseResponseDTO updateUserCourse(Long id, UserCourseRequestDTO userCourseDTO) {
        Optional<UserCourse> existingUserCourseOptional = userCourseRepository.findByIdAndDeletedFalse(id);

        if (existingUserCourseOptional.isEmpty()) {
            throw new UserCourseNotFoundException();
        }

        UserCourse existingUserCourse = existingUserCourseOptional.get();
        User user = userRepository.findByIdAndDeletedFalse(userCourseDTO.getUserId()).orElseThrow(UserNotFoundException::new);
        Course course = courseRepository.findByIdAndDeletedFalse(userCourseDTO.getCourseId()).orElseThrow(CourseNotFoundException::new);

        existingUserCourse.setUser(user);
        existingUserCourse.setCourse(course);

        try {
            existingUserCourse.setId(id);
            UserCourse updatedUserCourse = userCourseRepository.save(existingUserCourse);
            return modelMapper.map(updatedUserCourse, UserCourseResponseDTO.class);
        } catch (TransactionException exception) {
            if (exception.getRootCause() instanceof ConstraintViolationException validationException) {
                throw new com.coolSchool.CoolSchool.exceptions.userCourse.ValidationUserCourseException(validationException.getConstraintViolations());
            }
            throw exception;
        }
    }

    @Override
    public void deleteUserCourse(Long id) {
        Optional<UserCourse> userCourse = userCourseRepository.findByIdAndDeletedFalse(id);
        if (userCourse.isPresent()) {
            userCourse.get().setDeleted(true);
            userCourseRepository.save(userCourse.get());
        } else {
            throw new UserCourseNotFoundException();
        }
    }
}
