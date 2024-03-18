package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.CourseSubsectionController;
import com.coolSchool.coolSchool.models.dto.request.CourseSubsectionRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CourseSubsectionResponseDTO;
import com.coolSchool.coolSchool.services.CourseSubsectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CourseSubsectionControllerTest {

    @Mock
    private CourseSubsectionService courseSubsectionService;

    @InjectMocks
    private CourseSubsectionController courseSubsectionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseSubsectionController).build();
    }

    @Test
    void testGetAllCourseSubsections() throws Exception {
        List<CourseSubsectionResponseDTO> mockSubsections = Arrays.asList(
                new CourseSubsectionResponseDTO(),
                new CourseSubsectionResponseDTO()
        );
        mockSubsections.get(0).setId(1L);
        mockSubsections.get(0).setTitle("Subsection 1");
        mockSubsections.get(1).setId(2L);
        mockSubsections.get(1).setTitle("Subsection 2");

        when(courseSubsectionService.getAllCourseSubsections()).thenReturn(mockSubsections);

        mockMvc.perform(get("/api/v1/courseSubsections/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(mockSubsections.size()));
    }

    @Test
    void testGetAllByCourseId() throws Exception {
        Long courseId = 1L;
        List<CourseSubsectionResponseDTO> mockSubsections = Arrays.asList(
                new CourseSubsectionResponseDTO(),
                new CourseSubsectionResponseDTO()
        );
        mockSubsections.get(0).setId(1L);
        mockSubsections.get(0).setTitle("Subsection 1");
        mockSubsections.get(1).setId(2L);
        mockSubsections.get(1).setTitle("Subsection 2");

        when(courseSubsectionService.getAllByCourse(courseId)).thenReturn(mockSubsections);

        mockMvc.perform(get("/api/v1/courseSubsections/course/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(mockSubsections.size()));
    }

    @Test
    void testGetCourseSubsectionById() throws Exception {
        Long subsectionId = 1L;
        CourseSubsectionResponseDTO mockSubsection = new CourseSubsectionResponseDTO();
        mockSubsection.setId(1L);
        mockSubsection.setTitle("Subsection 1");

        when(courseSubsectionService.getCourseSubsectionById(subsectionId)).thenReturn(mockSubsection);

        mockMvc.perform(get("/api/v1/courseSubsections/{id}", subsectionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateCourseSubsection() throws Exception {
        CourseSubsectionRequestDTO requestDTO = new CourseSubsectionRequestDTO();
        requestDTO.setTitle("New Subsection");

        CourseSubsectionResponseDTO mockResponseDTO = new CourseSubsectionResponseDTO();
        mockResponseDTO.setId(1L);
        mockResponseDTO.setTitle("New Subsection");

        when(courseSubsectionService.createCourseSubsection(requestDTO)).thenReturn(mockResponseDTO);

        mockMvc.perform(post("/api/v1/courseSubsections/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"New Subsection\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testUpdateCourseSubsection() throws Exception {
        Long subsectionId = 1L;
        CourseSubsectionRequestDTO requestDTO = new CourseSubsectionRequestDTO();
        requestDTO.setDescription("Updated Subsection");

        CourseSubsectionResponseDTO mockResponseDTO = new CourseSubsectionResponseDTO();
        mockResponseDTO.setId(subsectionId);
        mockResponseDTO.setDescription("Updated Subsection");

        when(courseSubsectionService.updateCourseSubsection(subsectionId, requestDTO)).thenReturn(mockResponseDTO);

        mockMvc.perform(put("/api/v1/courseSubsections/{id}", subsectionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Subsection\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(subsectionId));
    }

    @Test
    void testDeleteCourseSubsectionById() throws Exception {
        Long subsectionId = 1L;

        mockMvc.perform(delete("/api/v1/courseSubsections/{id}", subsectionId))
                .andExpect(status().isOk())
                .andExpect(content().string("CourseSubsection with id: 1 has been deleted successfully!"));

        verify(courseSubsectionService, times(1)).deleteCourseSubsection(subsectionId);
    }
}

