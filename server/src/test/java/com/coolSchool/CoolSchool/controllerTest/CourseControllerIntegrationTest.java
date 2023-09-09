package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.CoolSchool.controllers.CourseController;
import com.coolSchool.CoolSchool.models.dto.CourseDTO;
import com.coolSchool.CoolSchool.services.impl.CourseServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(value = CourseController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = CourseController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
class CourseControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CourseServiceImpl courseService;
    private List<CourseDTO> courseList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        courseList = new ArrayList<>();
        courseList.add(new CourseDTO());
    }

    @Test
    void testGetAllCourses() throws Exception {
        Mockito.when(courseService.getAllCourses()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/courses/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetCourseById() throws Exception {
        Long courseId = 1L;
        CourseDTO course = new CourseDTO();

        Mockito.when(courseService.getCourseById(courseId)).thenReturn(course);

        mockMvc.perform(get("/api/v1/courses/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateCourse() throws Exception {
        CourseDTO course = new CourseDTO();
        String courseJson = objectMapper.writeValueAsString(course);

        Mockito.when(courseService.createCourse(Mockito.any(CourseDTO.class))).thenReturn(course);

        mockMvc.perform(post("/api/v1/courses/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateCourse() throws Exception {
        Long courseId = 1L;
        CourseDTO updatedCourse = new CourseDTO();
        String updatedCourseJson = objectMapper.writeValueAsString(updatedCourse);

        Mockito.when(courseService.updateCourse(Mockito.eq(courseId), Mockito.any(CourseDTO.class)))
                .thenReturn(updatedCourse);

        mockMvc.perform(put("/api/v1/courses/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCourseJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteCourseById() throws Exception {
        Long courseId = 1L;
        mockMvc.perform(delete("/api/v1/courses/{id}", courseId))
                .andExpect(status().isOk());
    }
}
