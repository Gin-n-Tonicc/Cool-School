package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.CoolSchool.exceptions.courseSubsection.CourseSubsectionNotFoundException;
import com.coolSchool.CoolSchool.exceptions.courseSubsection.ValidationCourseSubsectionException;
import com.coolSchool.CoolSchool.models.dto.request.CourseSubsectionRequestDTO;
import com.coolSchool.CoolSchool.models.dto.response.CourseSubsectionResponseDTO;
import com.coolSchool.CoolSchool.models.entity.Course;
import com.coolSchool.CoolSchool.models.entity.CourseSubsection;
import com.coolSchool.CoolSchool.repositories.CourseRepository;
import com.coolSchool.CoolSchool.repositories.CourseSubsectionRepository;
import com.coolSchool.CoolSchool.services.impl.CourseSubsectionServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseSubsectionServiceImplTest {

    @Mock
    private CourseSubsectionRepository courseSubsectionRepository;
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseSubsectionServiceImpl courseSubsectionService;

    private ModelMapper modelMapper;
    private Validator validator;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        courseSubsectionService = new CourseSubsectionServiceImpl(courseSubsectionRepository, modelMapper, courseRepository, validator);
    }

    @Test
    public void testDeleteCourseSubsection_CourseSubsectionPresent() {
        Long courseSubsectionId = 1L;

        CourseSubsection courseSubsection = new CourseSubsection();
        courseSubsection.setDeleted(false);

        Optional<CourseSubsection> courseSubsectionOptional = Optional.of(courseSubsection);

        when(courseSubsectionRepository.findByIdAndDeletedFalse(courseSubsectionId)).thenReturn(courseSubsectionOptional);
        when(courseSubsectionRepository.save(any(CourseSubsection.class))).thenReturn(courseSubsection);

        assertDoesNotThrow(() -> courseSubsectionService.deleteCourseSubsection(courseSubsectionId));
        assertTrue(courseSubsection.isDeleted());
        verify(courseSubsectionRepository, times(1)).save(courseSubsection);
    }

    @Test
    void testGetAllCategories() {
        List<CourseSubsection> courseSubsectionList = new ArrayList<>();
        courseSubsectionList.add(new CourseSubsection());
        Mockito.when(courseSubsectionRepository.findByDeletedFalse()).thenReturn(courseSubsectionList);
        List<CourseSubsectionResponseDTO> result = courseSubsectionService.getAllCourseSubsections();
        assertNotNull(result);
        assertEquals(courseSubsectionList.size(), result.size());
    }

    @Test
    void testGetCourseSubsectionById() {
        Long courseSubsectionId = 1L;
        CourseSubsection courseSubsection = new CourseSubsection();
        Optional<CourseSubsection> courseSubsectionOptional = Optional.of(courseSubsection);
        when(courseSubsectionRepository.findByIdAndDeletedFalse(courseSubsectionId)).thenReturn(courseSubsectionOptional);
        CourseSubsectionResponseDTO result = courseSubsectionService.getCourseSubsectionById(courseSubsectionId);
        assertNotNull(result);
    }

    @Test
    void testGetCourseSubsectionByIdNotFound() {
        Long courseSubsectionId = 1L;
        Optional<CourseSubsection> courseSubsectionOptional = Optional.empty();
        when(courseSubsectionRepository.findByIdAndDeletedFalse(courseSubsectionId)).thenReturn(courseSubsectionOptional);
        assertThrows(CourseSubsectionNotFoundException.class, () -> courseSubsectionService.getCourseSubsectionById(courseSubsectionId));
    }

    @Test
    void testCreateCourseSubsection() {
        CourseSubsectionResponseDTO courseSubsectionDTO = new CourseSubsectionResponseDTO();
        CourseSubsection courseSubsection = modelMapper.map(courseSubsectionDTO, CourseSubsection.class);
        when(courseSubsectionRepository.save(any(CourseSubsection.class))).thenReturn(courseSubsection);
        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        CourseSubsectionResponseDTO result = courseSubsectionService.createCourseSubsection(modelMapper.map(courseSubsectionDTO, CourseSubsectionRequestDTO.class));
        assertNotNull(result);
    }

    @Test
    void testUpdateCourseSubsection() {
        Long courseSubsectionId = 1L;
        CourseSubsectionResponseDTO updatedCourseSubsectionDTO = new CourseSubsectionResponseDTO();
        CourseSubsection existingCourseSubsection = new CourseSubsection();
        Optional<CourseSubsection> existingCourseSubsectionOptional = Optional.of(existingCourseSubsection);
        when(courseSubsectionRepository.findByIdAndDeletedFalse(courseSubsectionId)).thenReturn(existingCourseSubsectionOptional);
        when(courseSubsectionRepository.save(any(CourseSubsection.class))).thenReturn(existingCourseSubsection);
        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        CourseSubsectionResponseDTO result = courseSubsectionService.updateCourseSubsection(courseSubsectionId, modelMapper.map(updatedCourseSubsectionDTO, CourseSubsectionRequestDTO.class));
        assertNotNull(result);
    }

    @Test
    void testUpdateCourseSubsectionNotFound() {
        Long nonExistentCourseSubsectionId = 99L;
        CourseSubsectionResponseDTO updatedCourseSubsectionDTO = new CourseSubsectionResponseDTO();
        when(courseSubsectionRepository.findByIdAndDeletedFalse(nonExistentCourseSubsectionId)).thenReturn(Optional.empty());
        assertThrows(CourseSubsectionNotFoundException.class, () -> courseSubsectionService.updateCourseSubsection(nonExistentCourseSubsectionId, modelMapper.map(updatedCourseSubsectionDTO, CourseSubsectionRequestDTO.class)));
    }

    @Test
    void testDeleteCourseSubsectionNotFound() {
        Long nonExistentCourseSubsectionId = 99L;
        when(courseSubsectionRepository.findByIdAndDeletedFalse(nonExistentCourseSubsectionId)).thenReturn(Optional.empty());
        assertThrows(CourseSubsectionNotFoundException.class, () -> courseSubsectionService.deleteCourseSubsection(nonExistentCourseSubsectionId));
    }

    @Test
    void testCreateCourseSubsection_ValidationException() {
        CourseSubsectionResponseDTO courseSubsectionDTO = new CourseSubsectionResponseDTO();
        courseSubsectionDTO.setTitle(null);
        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);
        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);
        when(courseSubsectionRepository.save(any(CourseSubsection.class))).thenThrow(constraintViolationException);
        assertThrows(ValidationCourseSubsectionException.class, () -> courseSubsectionService.createCourseSubsection(modelMapper.map(courseSubsectionDTO, CourseSubsectionRequestDTO.class)));
    }

    @Test
    void testUpdateCourseSubsection_ValidationException() {
        Long courseSubsectionId = 1L;
        CourseSubsectionResponseDTO courseSubsectionDTO = new CourseSubsectionResponseDTO();
        courseSubsectionDTO.setDescription(null);
        CourseSubsection existingCourseSubsection = new CourseSubsection();
        Optional<CourseSubsection> existingCourseSubsectionOptional = Optional.of(existingCourseSubsection);
        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);
        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);
        when(courseSubsectionRepository.findByIdAndDeletedFalse(courseSubsectionId)).thenReturn(existingCourseSubsectionOptional);
        when(courseSubsectionRepository.save(any(CourseSubsection.class))).thenThrow(constraintViolationException);
        assertThrows(ConstraintViolationException.class, () -> courseSubsectionService.updateCourseSubsection(courseSubsectionId, modelMapper.map(courseSubsectionDTO, CourseSubsectionRequestDTO.class)));
    }
}

