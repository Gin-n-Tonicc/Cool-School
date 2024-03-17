package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.exceptions.course.CourseNotFoundException;
import com.coolSchool.coolSchool.exceptions.courseSubsection.CourseSubsectionNotFoundException;
import com.coolSchool.coolSchool.exceptions.files.FileNotFoundException;
import com.coolSchool.coolSchool.models.dto.request.CourseSubsectionRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CourseSubsectionResponseDTO;
import com.coolSchool.coolSchool.models.entity.CourseSubsection;
import com.coolSchool.coolSchool.models.entity.Resource;
import com.coolSchool.coolSchool.repositories.CourseRepository;
import com.coolSchool.coolSchool.repositories.CourseSubsectionRepository;
import com.coolSchool.coolSchool.repositories.ResourceRepository;
import com.coolSchool.coolSchool.services.CourseSubsectionService;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

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
    private final MessageSource messageSource;

    public CourseSubsectionServiceImpl(CourseSubsectionRepository courseSubsectionRepository, ModelMapper modelMapper, CourseRepository courseRepository, ResourceRepository resourceRepository, MessageSource messageSource) {
        this.courseSubsectionRepository = courseSubsectionRepository;
        this.modelMapper = modelMapper;
        this.courseRepository = courseRepository;
        this.resourceRepository = resourceRepository;
        this.messageSource = messageSource;
    }

    @Override
    public List<CourseSubsectionResponseDTO> getAllCourseSubsections() {
        List<CourseSubsection> courseSubsections = courseSubsectionRepository.findByDeletedFalse();
        return courseSubsections.stream().map(courseSubsection -> modelMapper.map(courseSubsection, CourseSubsectionResponseDTO.class)).toList();
    }

    @Override
    public List<CourseSubsectionResponseDTO> getAllByCourse(Long id) {
        // Get all the subsections in a course
        List<CourseSubsection> courseSubsections = courseSubsectionRepository.findAllByCourseIdAndDeletedFalse(id);
        return courseSubsections.stream().map(courseSubsection -> modelMapper.map(courseSubsection, CourseSubsectionResponseDTO.class)).toList();
    }

    @Override
    public CourseSubsectionResponseDTO getCourseSubsectionById(Long id) {
        Optional<CourseSubsection> courseSubsection = courseSubsectionRepository.findByIdAndDeletedFalse(id);
        if (courseSubsection.isPresent()) {
            return modelMapper.map(courseSubsection.get(), CourseSubsectionResponseDTO.class);
        }
        throw new CourseSubsectionNotFoundException(messageSource);
    }

    @Override
    public CourseSubsectionResponseDTO createCourseSubsection(CourseSubsectionRequestDTO courseSubsectionDTO) {
        courseSubsectionDTO.setId(null);
        courseRepository.findByIdAndDeletedFalse(courseSubsectionDTO.getCourseId()).orElseThrow(() -> new CourseNotFoundException(messageSource));
        CourseSubsection courseSubsectionEntity = courseSubsectionRepository.save(modelMapper.map(courseSubsectionDTO, CourseSubsection.class));
        return modelMapper.map(courseSubsectionEntity, CourseSubsectionResponseDTO.class);
    }

    @Override
    public CourseSubsectionResponseDTO updateCourseSubsection(Long id, CourseSubsectionRequestDTO courseSubsectionDTO) {
        Optional<CourseSubsection> existingCourseSubsectionOptional = courseSubsectionRepository.findByIdAndDeletedFalse(id);

        if (existingCourseSubsectionOptional.isEmpty()) {
            throw new CourseSubsectionNotFoundException(messageSource);
        }
        CourseSubsection subsection = existingCourseSubsectionOptional.get();
        Set<Resource> resources = subsection.getResources();
        if (courseSubsectionDTO.getResources() != null) {
            resources = courseSubsectionDTO.getResources().stream().map(x -> resourceRepository.findByIdAndDeletedFalse(x).orElseThrow(() -> new FileNotFoundException(messageSource))).collect(Collectors.toSet());
        }
        courseRepository.findByIdAndDeletedFalse(courseSubsectionDTO.getCourseId()).orElseThrow(() -> new CourseNotFoundException(messageSource));
        CourseSubsection existingCourseSubsection = existingCourseSubsectionOptional.get();
        modelMapper.map(courseSubsectionDTO, existingCourseSubsection);


        existingCourseSubsection.setId(id);
        existingCourseSubsection.setResources(resources);
        CourseSubsection updatedCourseSubsection = courseSubsectionRepository.save(existingCourseSubsection);
        return modelMapper.map(updatedCourseSubsection, CourseSubsectionResponseDTO.class);
    }

    @Override
    public void deleteCourseSubsection(Long id) {
        Optional<CourseSubsection> courseSubsection = courseSubsectionRepository.findByIdAndDeletedFalse(id);
        if (courseSubsection.isPresent()) {
            courseSubsection.get().setDeleted(true);
            courseSubsectionRepository.save(courseSubsection.get());
        } else {
            throw new CourseSubsectionNotFoundException(messageSource);
        }
    }
}
