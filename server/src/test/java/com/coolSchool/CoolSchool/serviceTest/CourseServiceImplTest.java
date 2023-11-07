package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.CoolSchool.exceptions.course.CourseNotFoundException;
import com.coolSchool.CoolSchool.exceptions.course.ValidationCourseException;
import com.coolSchool.CoolSchool.filters.JwtAuthenticationFilter;
import com.coolSchool.CoolSchool.models.dto.CourseDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.models.entity.Category;
import com.coolSchool.CoolSchool.models.entity.Course;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.repositories.CategoryRepository;
import com.coolSchool.CoolSchool.repositories.CourseRepository;
import com.coolSchool.CoolSchool.repositories.UserRepository;
import com.coolSchool.CoolSchool.services.impl.CourseServiceImpl;
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
class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private ModelMapper modelMapper;
    private Validator validator;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        courseService = new CourseServiceImpl(courseRepository, modelMapper, userRepository, categoryRepository, validator);
    }

    @Test
    public void testDeleteCourse_CoursePresent() {
        Long courseId = 1L;

        Course course = new Course();
        course.setDeleted(false);

        Optional<Course> courseOptional = Optional.of(course);

        when(courseRepository.findByIdAndDeletedFalse(courseId)).thenReturn(courseOptional);
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        assertDoesNotThrow(() -> courseService.deleteCourse(courseId, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
        assertTrue(course.isDeleted());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testGetAllCourses() {
        List<Course> courseList = new ArrayList<>();
        courseList.add(new Course());
        Mockito.when(courseRepository.findByDeletedFalse()).thenReturn(courseList);
        List<CourseDTO> result = courseService.getAllCourses();
        assertNotNull(result);
        assertEquals(courseList.size(), result.size());
    }

    @Test
    void testGetCourseById() {
        Long courseId = 1L;
        Course course = new Course();
        Optional<Course> courseOptional = Optional.of(course);
        when(courseRepository.findByIdAndDeletedFalse(courseId)).thenReturn(courseOptional);
        CourseDTO result = courseService.getCourseById(courseId);
        assertNotNull(result);
    }

    @Test
    void testGetCourseByIdNotFound() {
        Long courseId = 1L;
        Optional<Course> courseOptional = Optional.empty();
        when(courseRepository.findByIdAndDeletedFalse(courseId)).thenReturn(courseOptional);
        assertThrows(CourseNotFoundException.class, () -> courseService.getCourseById(courseId));
    }

    @Test
    void testCreateCourse() {
        CourseDTO courseDTO = new CourseDTO();
        Course course = modelMapper.map(courseDTO, Course.class);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new User()));
        when(categoryRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Category()));
        CourseDTO result = courseService.createCourse(courseDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        assertNotNull(result);
    }

    @Test
    void testUpdateCourse() {
        Long courseId = 1L;
        CourseDTO updatedCourseDTO = new CourseDTO();
        Course existingCourse = new Course();
        Optional<Course> existingCourseOptional = Optional.of(existingCourse);
        when(courseRepository.findByIdAndDeletedFalse(courseId)).thenReturn(existingCourseOptional);
        when(courseRepository.save(any(Course.class))).thenReturn(existingCourse);
        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new User()));
        when(categoryRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Category()));
        CourseDTO result = courseService.updateCourse(courseId, updatedCourseDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateCourseNotFound() {
        Long nonExistentCourseId = 99L;
        CourseDTO updatedCourseDTO = new CourseDTO();
        when(courseRepository.findByIdAndDeletedFalse(nonExistentCourseId)).thenReturn(Optional.empty());
        assertThrows(CourseNotFoundException.class, () -> courseService.updateCourse(nonExistentCourseId, updatedCourseDTO));
    }

    @Test
    void testDeleteCourseNotFound() {
        Long nonExistentCourseId = 99L;

        when(courseRepository.findByIdAndDeletedFalse(nonExistentCourseId)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.deleteCourse(nonExistentCourseId, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @Test
    void testCreateCourse_ValidationException() {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setName(null);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(courseRepository.save(any(Course.class))).thenThrow(constraintViolationException);
        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new User()));
        when(categoryRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Category()));
        assertThrows(ValidationCourseException.class, () -> courseService.createCourse(courseDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @Test
    void testUpdateCourse_ValidationException() {
        Long courseId = 1L;
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setCategoryId(null);
        Course existingCourse = new Course();
        Optional<Course> existingCourseOptional = Optional.of(existingCourse);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);
        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new User()));
        when(categoryRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Category()));
        when(courseRepository.findByIdAndDeletedFalse(courseId)).thenReturn(existingCourseOptional);
        when(courseRepository.save(any(Course.class))).thenThrow(constraintViolationException);

        assertThrows(ConstraintViolationException.class, () -> courseService.updateCourse(courseId, courseDTO));
    }
}

