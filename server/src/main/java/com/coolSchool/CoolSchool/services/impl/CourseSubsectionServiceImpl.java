package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.exceptions.common.NoSuchElementException;
import com.coolSchool.CoolSchool.exceptions.courseSubsection.CourseSubsectionNotFoundException;
import com.coolSchool.CoolSchool.exceptions.courseSubsection.ValidationCourseSubsectionException;
import com.coolSchool.CoolSchool.models.dto.CourseSubsectionDTO;
import com.coolSchool.CoolSchool.models.entity.CourseSubsection;
import com.coolSchool.CoolSchool.repositories.CourseRepository;
import com.coolSchool.CoolSchool.repositories.CourseSubsectionRepository;
import com.coolSchool.CoolSchool.services.CourseSubsectionService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.util.List;
import java.util.Optional;

@Service
public class CourseSubsectionServiceImpl implements CourseSubsectionService {
    private final CourseSubsectionRepository courseSubsectionRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;
    private final Validator validator;

    public CourseSubsectionServiceImpl(CourseSubsectionRepository courseSubsectionRepository, ModelMapper modelMapper, CourseRepository courseRepository, Validator validator) {
        this.courseSubsectionRepository = courseSubsectionRepository;
        this.modelMapper = modelMapper;
        this.courseRepository = courseRepository;
        this.validator = validator;
    }

    @Override
    public List<CourseSubsectionDTO> getAllCourseSubsections() {
        List<CourseSubsection> courseSubsections = courseSubsectionRepository.findByDeletedFalse();
        return courseSubsections.stream().map(courseSubsection -> modelMapper.map(courseSubsection, CourseSubsectionDTO.class)).toList();
    }

    @Override
    public CourseSubsectionDTO getCourseSubsectionById(Long id) {
        Optional<CourseSubsection> courseSubsection = courseSubsectionRepository.findByIdAndDeletedFalse(id);
        if (courseSubsection.isPresent()) {
            return modelMapper.map(courseSubsection.get(), CourseSubsectionDTO.class);
        }
        throw new CourseSubsectionNotFoundException();
    }

    @Override
    public CourseSubsectionDTO createCourseSubsection(CourseSubsectionDTO courseSubsectionDTO) {
        try {
            courseSubsectionDTO.setId(null);
            courseRepository.findByIdAndDeletedFalse(courseSubsectionDTO.getCourseId()).orElseThrow(NoSuchElementException::new);
            CourseSubsection courseSubsectionEntity = courseSubsectionRepository.save(modelMapper.map(courseSubsectionDTO, CourseSubsection.class));
            return modelMapper.map(courseSubsectionEntity, CourseSubsectionDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationCourseSubsectionException(exception.getConstraintViolations());
        }
    }

    @Override
    public CourseSubsectionDTO updateCourseSubsection(Long id, CourseSubsectionDTO courseSubsectionDTO) {
        Optional<CourseSubsection> existingCourseSubsectionOptional = courseSubsectionRepository.findByIdAndDeletedFalse(id);

        if (existingCourseSubsectionOptional.isEmpty()) {
            throw new CourseSubsectionNotFoundException();
        }
        courseRepository.findByIdAndDeletedFalse(courseSubsectionDTO.getCourseId()).orElseThrow(NoSuchElementException::new);
        CourseSubsection existingCourseSubsection = existingCourseSubsectionOptional.get();
        modelMapper.map(courseSubsectionDTO, existingCourseSubsection);

        try {
            existingCourseSubsection.setId(id);
            CourseSubsection updatedCourseSubsection = courseSubsectionRepository.save(existingCourseSubsection);
            return modelMapper.map(updatedCourseSubsection, CourseSubsectionDTO.class);
        } catch (TransactionException exception) {
            if (exception.getRootCause() instanceof ConstraintViolationException validationException) {
                throw new ValidationCourseSubsectionException(validationException.getConstraintViolations());
            }
            throw exception;
        }
    }

    @Override
    public void deleteCourseSubsection(Long id) {
        Optional<CourseSubsection> courseSubsection = courseSubsectionRepository.findByIdAndDeletedFalse(id);
        if (courseSubsection.isPresent()) {
            courseSubsection.get().setDeleted(true);
            courseSubsectionRepository.save(courseSubsection.get());
        } else {
            throw new CourseSubsectionNotFoundException();
        }
    }
}
