package com.coolSchool.CoolSchool.serviceTest;


import com.coolSchool.coolSchool.exceptions.courseSubsection.CourseSubsectionNotFoundException;
import com.coolSchool.coolSchool.models.dto.common.CourseSubsectionDTO;
import com.coolSchool.coolSchool.models.dto.request.CourseSubsectionRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CourseSubsectionResponseDTO;
import com.coolSchool.coolSchool.models.entity.Course;
import com.coolSchool.coolSchool.models.entity.CourseSubsection;
import com.coolSchool.coolSchool.repositories.CourseRepository;
import com.coolSchool.coolSchool.repositories.CourseSubsectionRepository;
import com.coolSchool.coolSchool.repositories.ResourceRepository;
import com.coolSchool.coolSchool.services.impl.CourseSubsectionServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

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

    @Mock
    private ResourceRepository resourceRepository;
    @InjectMocks
    private CourseSubsectionServiceImpl courseSubsectionService;

    private ModelMapper modelMapper;
    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        courseSubsectionService = new CourseSubsectionServiceImpl(courseSubsectionRepository, modelMapper, courseRepository, resourceRepository, messageSource);
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
    void testGetAllCourseSubsections() {
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
        CourseSubsectionRequestDTO courseSubsectionDTO = new CourseSubsectionRequestDTO();
        CourseSubsection courseSubsection = modelMapper.map(courseSubsectionDTO, CourseSubsection.class);

        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        when(courseSubsectionRepository.save(any(CourseSubsection.class))).thenReturn(courseSubsection);
        CourseSubsectionResponseDTO result = courseSubsectionService.createCourseSubsection(courseSubsectionDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateCourseSubsection() {
        Long courseSubsectionId = 1L;
        CourseSubsectionRequestDTO updatedCourseSubsectionDTO = new CourseSubsectionRequestDTO();
        CourseSubsection existingCourseSubsection = new CourseSubsection();
        Optional<CourseSubsection> existingCourseSubsectionOptional = Optional.of(existingCourseSubsection);

        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        when(courseSubsectionRepository.findByIdAndDeletedFalse(courseSubsectionId)).thenReturn(existingCourseSubsectionOptional);
        when(courseSubsectionRepository.save(any(CourseSubsection.class))).thenReturn(existingCourseSubsection);
        CourseSubsectionDTO result = courseSubsectionService.updateCourseSubsection(courseSubsectionId, updatedCourseSubsectionDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateCourseSubsectionNotFound() {
        Long nonExistentCourseSubsectionId = 99L;
        CourseSubsectionRequestDTO updatedCourseSubsectionDTO = new CourseSubsectionRequestDTO();
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
        CourseSubsectionRequestDTO courseSubsectionDTO = new CourseSubsectionRequestDTO();
        courseSubsectionDTO.setDescription(null);
        courseSubsectionDTO.setTitle(null);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        when(courseSubsectionRepository.save(any(CourseSubsection.class))).thenThrow(constraintViolationException);

        assertThrows(ConstraintViolationException.class, () -> courseSubsectionService.createCourseSubsection(courseSubsectionDTO));
    }

    @Test
    void testUpdateCourseSubsection_ValidationException() {
        Long courseSubsectionId = 1L;
        CourseSubsectionRequestDTO courseSubsectionDTO = new CourseSubsectionRequestDTO();
        courseSubsectionDTO.setTitle(null);
        CourseSubsection existingCourseSubsection = new CourseSubsection();
        Optional<CourseSubsection> existingCourseSubsectionOptional = Optional.of(existingCourseSubsection);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        when(courseSubsectionRepository.findByIdAndDeletedFalse(courseSubsectionId)).thenReturn(existingCourseSubsectionOptional);
        when(courseSubsectionRepository.save(any(CourseSubsection.class))).thenThrow(constraintViolationException);

        assertThrows(ConstraintViolationException.class, () -> courseSubsectionService.updateCourseSubsection(courseSubsectionId, courseSubsectionDTO));
    }

    @Test
    void testCreateCourseSubsection_DataIntegrityViolationException() {
        CourseSubsectionRequestDTO courseSubsectionDTO = new CourseSubsectionRequestDTO();
        courseSubsectionDTO.setId(1L);
        courseSubsectionDTO.setTitle("Test CourseSubsection");

        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Course()));
        when(courseSubsectionRepository.save(any(CourseSubsection.class))).thenThrow(CourseSubsectionNotFoundException.class);

        assertThrows(CourseSubsectionNotFoundException.class, () -> courseSubsectionService.createCourseSubsection(courseSubsectionDTO));
    }

    @Test
    void testGetAllByCourse() {
        Long courseId = 1L;
        List<CourseSubsection> expectedSubsections = new ArrayList<>();
        expectedSubsections.add(new CourseSubsection());
        when(courseSubsectionRepository.findAllByCourseIdAndDeletedFalse(courseId)).thenReturn(expectedSubsections);

        List<CourseSubsectionResponseDTO> result = courseSubsectionService.getAllByCourse(courseId);

        assertNotNull(result);
        assertEquals(expectedSubsections.size(), result.size());
    }
}
