package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.exceptions.course.CourseNotFoundException;
import com.coolSchool.CoolSchool.exceptions.courseSubsection.CourseSubsectionNotFoundException;
import com.coolSchool.CoolSchool.exceptions.courseSubsection.ValidationCourseSubsectionException;
import com.coolSchool.CoolSchool.exceptions.files.FileNotFoundException;
import com.coolSchool.CoolSchool.models.dto.request.CourseSubsectionRequestDTO;
import com.coolSchool.CoolSchool.models.dto.response.CourseSubsectionResponseDTO;
import com.coolSchool.CoolSchool.models.entity.Course;
import com.coolSchool.CoolSchool.models.entity.CourseSubsection;
import com.coolSchool.CoolSchool.models.entity.Resource;
import com.coolSchool.CoolSchool.repositories.CourseRepository;
import com.coolSchool.CoolSchool.repositories.CourseSubsectionRepository;
import com.coolSchool.CoolSchool.repositories.ResourceRepository;
import com.coolSchool.CoolSchool.services.CourseSubsectionService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CourseSubsectionServiceImpl implements CourseSubsectionService {
    private final CourseSubsectionRepository courseSubsectionRepository;
    private final ModelMapper modelMapper;
    private final CourseRepository courseRepository;
    private final ResourceRepository resourceRepository;
    private final Validator validator;

    public CourseSubsectionServiceImpl(CourseSubsectionRepository courseSubsectionRepository, ModelMapper modelMapper, CourseRepository courseRepository, Validator validator, ResourceRepository resourceRepository) {
        this.courseSubsectionRepository = courseSubsectionRepository;
        this.modelMapper = modelMapper;
        this.courseRepository = courseRepository;
        this.resourceRepository = resourceRepository;
        this.validator = validator;
    }

    @Override
    public List<CourseSubsectionResponseDTO> getAllCourseSubsections() {
        List<CourseSubsection> courseSubsections = courseSubsectionRepository.findByDeletedFalse();
        return courseSubsections.stream().map(courseSubsection -> modelMapper.map(courseSubsection, CourseSubsectionResponseDTO.class)).toList();
    }

    @Override
    public List<CourseSubsectionResponseDTO> getAllByCourse(Long id) {
        List<CourseSubsection> courseSubsections = courseSubsectionRepository.findAllByCourseIdAndDeletedFalse(id);
        return courseSubsections.stream().map(courseSubsection -> modelMapper.map(courseSubsection, CourseSubsectionResponseDTO.class)).toList();
    }

    @Override
    public CourseSubsectionResponseDTO getCourseSubsectionById(Long id) {
        Optional<CourseSubsection> courseSubsection = courseSubsectionRepository.findByIdAndDeletedFalse(id);
        if (courseSubsection.isPresent()) {
            return modelMapper.map(courseSubsection.get(), CourseSubsectionResponseDTO.class);
        }
        throw new CourseSubsectionNotFoundException();
    }

    @Override
    public CourseSubsectionResponseDTO createCourseSubsection(CourseSubsectionRequestDTO courseSubsectionDTO) {
        try {
            courseSubsectionDTO.setId(null);
            courseRepository.findByIdAndDeletedFalse(courseSubsectionDTO.getCourseId()).orElseThrow(CourseNotFoundException::new);
            CourseSubsection courseSubsectionEntity = courseSubsectionRepository.save(modelMapper.map(courseSubsectionDTO, CourseSubsection.class));
            return modelMapper.map(courseSubsectionEntity, CourseSubsectionResponseDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationCourseSubsectionException(exception.getConstraintViolations());
        }
    }

    @Override
    public CourseSubsectionResponseDTO updateCourseSubsection(Long id, CourseSubsectionRequestDTO courseSubsectionDTO) {
        Optional<CourseSubsection> existingCourseSubsectionOptional = courseSubsectionRepository.findByIdAndDeletedFalse(id);

        if (existingCourseSubsectionOptional.isEmpty()) {
            throw new CourseSubsectionNotFoundException();
        }
        CourseSubsection subsection = existingCourseSubsectionOptional.get();
        Set<Resource> resources = subsection.getResources();
        if(courseSubsectionDTO.getResources()!=null){
            resources = courseSubsectionDTO.getResources().stream().map(x -> resourceRepository.findByIdAndDeletedFalse(x).orElseThrow(FileNotFoundException::new)).collect(Collectors.toSet());
        }
        courseRepository.findByIdAndDeletedFalse(courseSubsectionDTO.getCourseId()).orElseThrow(CourseNotFoundException::new);
        CourseSubsection existingCourseSubsection = existingCourseSubsectionOptional.get();
        modelMapper.map(courseSubsectionDTO, existingCourseSubsection);

        try {
            existingCourseSubsection.setId(id);
            existingCourseSubsection.setResources(resources);
            CourseSubsection updatedCourseSubsection = courseSubsectionRepository.save(existingCourseSubsection);
            return modelMapper.map(updatedCourseSubsection, CourseSubsectionResponseDTO.class);
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
