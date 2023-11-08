package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.enums.Role;
import com.coolSchool.CoolSchool.exceptions.common.AccessDeniedException;
import com.coolSchool.CoolSchool.exceptions.common.NoSuchElementException;
import com.coolSchool.CoolSchool.exceptions.course.CourseNotFoundException;
import com.coolSchool.CoolSchool.exceptions.course.ValidationCourseException;
import com.coolSchool.CoolSchool.models.dto.CourseDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.entity.Course;
import com.coolSchool.CoolSchool.repositories.CategoryRepository;
import com.coolSchool.CoolSchool.repositories.CourseRepository;
import com.coolSchool.CoolSchool.repositories.UserRepository;
import com.coolSchool.CoolSchool.services.CourseService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final Validator validator;

    public CourseServiceImpl(CourseRepository courseRepository, ModelMapper modelMapper, UserRepository userRepository, CategoryRepository categoryRepository, Validator validator) {
        this.courseRepository = courseRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.validator = validator;
    }

    @Override
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findByDeletedFalse();
        return courses.stream().map(course -> modelMapper.map(course, CourseDTO.class)).toList();
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        Optional<Course> course = courseRepository.findByIdAndDeletedFalse(id);
        if (course.isPresent()) {
            return modelMapper.map(course.get(), CourseDTO.class);
        }
        throw new CourseNotFoundException();
    }

    @Override
    public CourseDTO createCourse(CourseDTO courseDTO, PublicUserDTO loggedUser) {
        if (loggedUser == null || !(loggedUser.getRole().equals(Role.ADMIN) || loggedUser.getRole().equals(Role.TEACHER))) {
            throw new AccessDeniedException();
        }
        try {
            courseDTO.setId(null);
            courseDTO.setStarts(0);
            userRepository.findByIdAndDeletedFalse(courseDTO.getUserId()).orElseThrow(NoSuchElementException::new);
            categoryRepository.findByIdAndDeletedFalse(courseDTO.getCategoryId()).orElseThrow(NoSuchElementException::new);
            Course courseEntity = courseRepository.save(modelMapper.map(courseDTO, Course.class));
            return modelMapper.map(courseEntity, CourseDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationCourseException(exception.getConstraintViolations());
        }
    }

    @Override
    public CourseDTO updateCourse(Long id, CourseDTO courseDTO, PublicUserDTO loggedUser) {
        Optional<Course> existingCourseOptional = courseRepository.findByIdAndDeletedFalse(id);

        if (existingCourseOptional.isEmpty()) {
            throw new CourseNotFoundException();
        }
        if (loggedUser == null || (!Objects.equals(loggedUser.getId(), courseDTO.getUserId()) && !(loggedUser.getRole().equals(Role.ADMIN)))) {
            throw new AccessDeniedException();
        }
        userRepository.findByIdAndDeletedFalse(courseDTO.getUserId()).orElseThrow(NoSuchElementException::new);
        categoryRepository.findByIdAndDeletedFalse(courseDTO.getCategoryId()).orElseThrow(NoSuchElementException::new);
        Course existingCourse = existingCourseOptional.get();
        modelMapper.map(courseDTO, existingCourse);

        try {
            existingCourse.setId(id);
            Course updatedCourse = courseRepository.save(existingCourse);
            return modelMapper.map(updatedCourse, CourseDTO.class);
        } catch (TransactionException exception) {
            if (exception.getRootCause() instanceof ConstraintViolationException validationException) {
                throw new ValidationCourseException(validationException.getConstraintViolations());
            }
            throw exception;
        }
    }

    @Override
    public void deleteCourse(Long id, PublicUserDTO loggedUser) {
        Optional<Course> course = courseRepository.findByIdAndDeletedFalse(id);
        if (course.isPresent()) {
            if (loggedUser == null || (!Objects.equals(loggedUser.getId(), course.get().getUser().getId()) && !(loggedUser.getRole().equals(Role.ADMIN)))) {
                throw new AccessDeniedException();
            }
            course.get().setDeleted(true);
            courseRepository.save(course.get());
        } else {
            throw new CourseNotFoundException();
        }
    }
}
