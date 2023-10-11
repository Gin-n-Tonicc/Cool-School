package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.CoolSchool.controllers.CourseSubsectionController;
import com.coolSchool.CoolSchool.models.dto.CourseSubsectionDTO;
import com.coolSchool.CoolSchool.services.impl.CourseSubsectionServiceImpl;
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
@WebMvcTest(value = CourseSubsectionController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = CourseSubsectionController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
class CourseSubsectionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CourseSubsectionServiceImpl courseSubsectionService;
    private List<CourseSubsectionDTO> courseSubsectionList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        courseSubsectionList = new ArrayList<>();
        courseSubsectionList.add(new CourseSubsectionDTO());
    }

    @Test
    void testGetAllCategories() throws Exception {
        Mockito.when(courseSubsectionService.getAllCourseSubsections()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/courseSubsections/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetCourseSubsectionById() throws Exception {
        Long courseSubsectionId = 1L;
        CourseSubsectionDTO courseSubsection = new CourseSubsectionDTO();

        Mockito.when(courseSubsectionService.getCourseSubsectionById(courseSubsectionId)).thenReturn(courseSubsection);

        mockMvc.perform(get("/api/v1/courseSubsections/{id}", courseSubsectionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateCourseSubsection() throws Exception {
        CourseSubsectionDTO courseSubsection = new CourseSubsectionDTO();
        String courseSubsectionJson = objectMapper.writeValueAsString(courseSubsection);

        Mockito.when(courseSubsectionService.createCourseSubsection(Mockito.any(CourseSubsectionDTO.class))).thenReturn(courseSubsection);

        mockMvc.perform(post("/api/v1/courseSubsections/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(courseSubsectionJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateCourseSubsection() throws Exception {
        Long courseSubsectionId = 1L;
        CourseSubsectionDTO updatedCourseSubsection = new CourseSubsectionDTO();
        String updatedCourseSubsectionJson = objectMapper.writeValueAsString(updatedCourseSubsection);

        Mockito.when(courseSubsectionService.updateCourseSubsection(Mockito.eq(courseSubsectionId), Mockito.any(CourseSubsectionDTO.class)))
                .thenReturn(updatedCourseSubsection);

        mockMvc.perform(put("/api/v1/courseSubsections/{id}", courseSubsectionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedCourseSubsectionJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteCourseSubsectionById() throws Exception {
        Long courseSubsectionId = 1L;
        mockMvc.perform(delete("/api/v1/courseSubsections/{id}", courseSubsectionId))
                .andExpect(status().isOk());
    }
}
