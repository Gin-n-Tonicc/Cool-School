package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.UserCourseController;
import com.coolSchool.coolSchool.models.dto.request.UserCourseRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.UserCourseResponseDTO;
import com.coolSchool.coolSchool.services.UserCourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UserCourseControllerIntegrationTest {

    @Mock
    UserCourseService userCourseService;

    @InjectMocks
    UserCourseController userCourseController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testCreateUserCourse() {
        UserCourseRequestDTO userCourseDTO = new UserCourseRequestDTO();
        ResponseEntity<UserCourseResponseDTO> expectedResponseEntity = new ResponseEntity<>(HttpStatus.CREATED);

        when(userCourseService.createUserCourse(userCourseDTO)).thenReturn(new UserCourseResponseDTO());

        ResponseEntity<UserCourseResponseDTO> responseEntity = userCourseController.createUserCourse(userCourseDTO);

        verify(userCourseService, times(1)).createUserCourse(userCourseDTO);

        assertEquals(expectedResponseEntity.getStatusCode(), responseEntity.getStatusCode());
    }

    @Test
    void testUpdateUserCourse() {
        Long courseId = 1L;
        UserCourseRequestDTO userCourseDTO = new UserCourseRequestDTO();
        ResponseEntity<UserCourseResponseDTO> expectedResponseEntity = new ResponseEntity<>(HttpStatus.OK);

        when(userCourseService.updateUserCourse(courseId, userCourseDTO)).thenReturn(new UserCourseResponseDTO());

        ResponseEntity<UserCourseResponseDTO> responseEntity = userCourseController.updateUserCourse(courseId, userCourseDTO);

        verify(userCourseService, times(1)).updateUserCourse(courseId, userCourseDTO);

        assertEquals(expectedResponseEntity.getStatusCode(), responseEntity.getStatusCode());
    }

    @Test
    void testDeleteUserCourseById() {
        Long courseId = 1L;
        ResponseEntity<String> expectedResponseEntity = new ResponseEntity<>("UserCourse with id: " + courseId + " has been deleted successfully!", HttpStatus.OK);

        ResponseEntity<String> responseEntity = userCourseController.deleteUserCourseById(courseId);

        verify(userCourseService, times(1)).deleteUserCourse(courseId);

        assertEquals(expectedResponseEntity.getStatusCode(), responseEntity.getStatusCode());
        assertEquals(expectedResponseEntity.getBody(), responseEntity.getBody());
    }

    @Test
    void testGetAllUserCourses() {
        List<UserCourseResponseDTO> userCourses = Arrays.asList(
                new UserCourseResponseDTO(),
                new UserCourseResponseDTO()
        );

        when(userCourseService.getAllUserCourses()).thenReturn(userCourses);

        ResponseEntity<List<UserCourseResponseDTO>> responseEntity = userCourseController.getAllUserCourses();

        verify(userCourseService, times(1)).getAllUserCourses();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userCourses, responseEntity.getBody());
    }

    @Test
    void testGetUserCourseById() {
        Long courseId = 1L;
        UserCourseResponseDTO userCourse = new UserCourseResponseDTO();

        when(userCourseService.getUserCourseById(courseId)).thenReturn(userCourse);

        ResponseEntity<UserCourseResponseDTO> responseEntity = userCourseController.getUserCourseById(courseId);

        verify(userCourseService, times(1)).getUserCourseById(courseId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(userCourse, responseEntity.getBody());
    }

}
