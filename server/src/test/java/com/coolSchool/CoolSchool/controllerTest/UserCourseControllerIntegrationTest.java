package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.UserCourseController;
import com.coolSchool.coolSchool.models.dto.request.UserCourseRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.UserCourseResponseDTO;
import com.coolSchool.coolSchool.services.impl.UserCourseServiceImpl;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration
@WebMvcTest(value = UserCourseController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = UserCourseController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
class UserCourseControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserCourseServiceImpl userCourseService;
    private List<UserCourseResponseDTO> userCourseList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        userCourseList = new ArrayList<UserCourseResponseDTO>();
        userCourseList.add(new UserCourseResponseDTO());
    }

    @Test
    void testGetAllUserCourses() throws Exception {
        Mockito.when(userCourseService.getAllUserCourses()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/userCourses/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetUserCourseById() throws Exception {
        Long userCourseId = 1L;
        UserCourseResponseDTO userCourse = new UserCourseResponseDTO();

        Mockito.when(userCourseService.getUserCourseById(userCourseId)).thenReturn(userCourse);

        mockMvc.perform(get("/api/v1/userCourses/{id}", userCourseId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateUserCourse() throws Exception {
        UserCourseResponseDTO userCourse = new UserCourseResponseDTO();
        String userCourseJson = objectMapper.writeValueAsString(userCourse);

        Mockito.when(userCourseService.createUserCourse(Mockito.any(UserCourseRequestDTO.class))).thenReturn(userCourse);

        mockMvc.perform(post("/api/v1/userCourses/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userCourseJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateUserCourse() throws Exception {
        Long userCourseId = 1L;
        UserCourseResponseDTO updatedUserCourse = new UserCourseResponseDTO();
        String updatedUserCourseJson = objectMapper.writeValueAsString(updatedUserCourse);

        Mockito.when(userCourseService.updateUserCourse(Mockito.eq(userCourseId), Mockito.any(UserCourseRequestDTO.class)))
                .thenReturn(updatedUserCourse);

        mockMvc.perform(put("/api/v1/userCourses/{id}", userCourseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserCourseJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteUserCourseById() throws Exception {
        Long userCourseId = 1L;
        mockMvc.perform(delete("/api/v1/userCourses/{id}", userCourseId))
                .andExpect(status().isOk());
    }
}
