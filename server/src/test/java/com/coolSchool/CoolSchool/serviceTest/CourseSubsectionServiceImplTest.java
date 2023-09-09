package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.CoolSchool.exceptions.courseSubsection.CourseSubsectionNotFoundException;
import com.coolSchool.CoolSchool.exceptions.courseSubsection.ValidationCourseSubsectionException;
import com.coolSchool.CoolSchool.models.dto.CourseSubsectionDTO;
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
        List<CourseSubsectionDTO> result = courseSubsectionService.getAllCourseSubsections();
        assertNotNull(result);
        assertEquals(courseSubsectionList.size(), result.size());
    }

    @Test
    void testGetCourseSubsectionById() {
        Long courseSubsectionId = 1L;
        CourseSubsection courseSubsection = new CourseSubsection();
        Optional<CourseSubsection> courseSubsectionOptional = Optional.of(courseSubsection);
        when(courseSubsectionRepository.findByIdAndDeletedFalse(courseSubsectionId)).thenReturn(courseSubsectionOptional);
        CourseSubsectionDTO result = courseSubsectionService.getCourseSubsectionById(courseSubsectionId);
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
        CourseSubsectionDTO courseSubsectionDTO = new CourseSubsectionDTO();
        CourseSubsection courseSubsection = modelMapper.map(courseSubsectionDTO, CourseSubsection.class);
        when(courseSubsectionRepository.save(any(CourseSubsection.class))).thenReturn(courseSubsection);
        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        CourseSubsectionDTO result = courseSubsectionService.createCourseSubsection(courseSubsectionDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateCourseSubsection() {
        Long courseSubsectionId = 1L;
        CourseSubsectionDTO updatedCourseSubsectionDTO = new CourseSubsectionDTO();
        CourseSubsection existingCourseSubsection = new CourseSubsection();
        Optional<CourseSubsection> existingCourseSubsectionOptional = Optional.of(existingCourseSubsection);
        when(courseSubsectionRepository.findByIdAndDeletedFalse(courseSubsectionId)).thenReturn(existingCourseSubsectionOptional);
        when(courseSubsectionRepository.save(any(CourseSubsection.class))).thenReturn(existingCourseSubsection);
        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        CourseSubsectionDTO result = courseSubsectionService.updateCourseSubsection(courseSubsectionId, updatedCourseSubsectionDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateCourseSubsectionNotFound() {
        Long nonExistentCourseSubsectionId = 99L;
        CourseSubsectionDTO updatedCourseSubsectionDTO = new CourseSubsectionDTO();
        when(courseSubsectionRepository.findByIdAndDeletedFalse(nonExistentCourseSubsectionId)).thenReturn(Optional.empty());
        assertThrows(CourseSubsectionNotFoundException.class, () -> courseSubsectionService.updateCourseSubsection(nonExistentCourseSubsectionId, updatedCourseSubsectionDTO));
    }

    @Test
    void testDeleteCourseSubsectionNotFound() {
        Long nonExistentCourseSubsectionId = 99L;
        when(courseSubsectionRepository.findByIdAndDeletedFalse(nonExistentCourseSubsectionId)).thenReturn(Optional.empty());
        assertThrows(CourseSubsectionNotFoundException.class, () -> courseSubsectionService.deleteCourseSubsection(nonExistentCourseSubsectionId));
    }

    @Test
    void testCreateCourseSubsection_ValidationException() {
        CourseSubsectionDTO courseSubsectionDTO = new CourseSubsectionDTO();
        courseSubsectionDTO.setTitle(null);
        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);
        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);
        when(courseSubsectionRepository.save(any(CourseSubsection.class))).thenThrow(constraintViolationException);
        assertThrows(ValidationCourseSubsectionException.class, () -> courseSubsectionService.createCourseSubsection(courseSubsectionDTO));
    }

    @Test
    void testUpdateCourseSubsection_ValidationException() {
        Long courseSubsectionId = 1L;
        CourseSubsectionDTO courseSubsectionDTO = new CourseSubsectionDTO();
        courseSubsectionDTO.setDescription(null);
        CourseSubsection existingCourseSubsection = new CourseSubsection();
        Optional<CourseSubsection> existingCourseSubsectionOptional = Optional.of(existingCourseSubsection);
        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);
        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);
        when(courseSubsectionRepository.findByIdAndDeletedFalse(courseSubsectionId)).thenReturn(existingCourseSubsectionOptional);
        when(courseSubsectionRepository.save(any(CourseSubsection.class))).thenThrow(constraintViolationException);
        assertThrows(ConstraintViolationException.class, () -> courseSubsectionService.updateCourseSubsection(courseSubsectionId, courseSubsectionDTO));
    }
}

