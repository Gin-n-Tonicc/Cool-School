package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.CourseController;
import com.coolSchool.coolSchool.exceptions.answer.filters.JwtAuthenticationFilter;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.CategoryDTO;
import com.coolSchool.coolSchool.models.dto.request.CourseRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CourseResponseDTO;
import com.coolSchool.coolSchool.models.entity.File;
import com.coolSchool.coolSchool.services.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CourseControllerIntegrationTest {
    @Mock
    CourseService courseService;

    @InjectMocks
    CourseController courseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllCourses() {
        List<CourseResponseDTO> courses = new ArrayList<>();
        when(courseService.getAllCourses()).thenReturn(courses);

        ResponseEntity<List<CourseResponseDTO>> responseEntity = courseController.getAllCourses();

        verify(courseService, times(1)).getAllCourses();
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(courses, responseEntity.getBody());
    }

    @Test
    void testGetCourseById() {
        Long courseId = 1L;
        CourseResponseDTO course = new CourseResponseDTO();
        when(courseService.getCourseById(courseId)).thenReturn(course);

        ResponseEntity<CourseResponseDTO> responseEntity = courseController.getCourseById(courseId);

        verify(courseService, times(1)).getCourseById(courseId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(course, responseEntity.getBody());
    }

    @Test
    void testCanEnroll() {
        Long courseId = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);

        PublicUserDTO user = new PublicUserDTO();

        when(request.getAttribute(JwtAuthenticationFilter.userKey)).thenReturn(user);

        when(courseService.canEnrollCourse(courseId, user)).thenReturn(true);

        ResponseEntity<Boolean> responseEntity = courseController.canEnroll(courseId, request);

        verify(courseService, times(1)).canEnrollCourse(courseId, user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(Boolean.TRUE, responseEntity.getBody());
    }


    @Test
    void testEnrollCourse() {
        Long courseId = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);

        PublicUserDTO user = new PublicUserDTO();

        when(request.getAttribute(JwtAuthenticationFilter.userKey)).thenReturn(user);

        ResponseEntity<Void> responseEntity = courseController.enrollCourse(courseId, request);

        verify(courseService, times(1)).enrollCourse(courseId, user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    void testCreateCourse() {
        CourseRequestDTO courseDTO = new CourseRequestDTO();
        HttpServletRequest request = mock(HttpServletRequest.class);
        PublicUserDTO user = new PublicUserDTO();

        CourseResponseDTO createdCourse = new CourseResponseDTO();
        createdCourse.setUser(user);
        createdCourse.setCategory(new CategoryDTO(1L, "Math"));
        createdCourse.setPicture(new File());

        when(request.getAttribute(JwtAuthenticationFilter.userKey)).thenReturn(user);

        when(courseService.createCourse(courseDTO, user)).thenReturn(createdCourse);

        ResponseEntity<CourseResponseDTO> responseEntity = courseController.createCourse(courseDTO, request);

        verify(courseService, times(1)).createCourse(courseDTO, user);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateCourse() {
        Long courseId = 1L;
        CourseRequestDTO courseDTO = new CourseRequestDTO();
        HttpServletRequest request = mock(HttpServletRequest.class);
        PublicUserDTO user = new PublicUserDTO();
        CourseResponseDTO updatedCourse = new CourseResponseDTO();

        when(request.getAttribute(JwtAuthenticationFilter.userKey)).thenReturn(user);

        when(courseService.updateCourse(courseId, courseDTO, user)).thenReturn(updatedCourse);

        ResponseEntity<CourseResponseDTO> responseEntity = courseController.updateCourse(courseId, courseDTO, request);

        verify(courseService, times(1)).updateCourse(courseId, courseDTO, user);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }


    @Test
    void testDeleteCourseById() {
        Long courseId = 1L;
        HttpServletRequest request = mock(HttpServletRequest.class);

        ResponseEntity<String> responseEntity = courseController.deleteCourseById(courseId, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Course with id: " + courseId + " has been deleted successfully!", responseEntity.getBody());
    }
}
