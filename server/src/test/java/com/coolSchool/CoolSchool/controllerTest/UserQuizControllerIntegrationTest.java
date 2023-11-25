package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.CoolSchool.controllers.UserQuizController;
import com.coolSchool.CoolSchool.models.dto.common.UserQuizDTO;
import com.coolSchool.CoolSchool.services.impl.UserQuizServiceImpl;
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
@WebMvcTest(value = UserQuizController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = UserQuizController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
class UserQuizControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserQuizServiceImpl userQuizService;
    private List<UserQuizDTO> userQuizList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        userQuizList = new ArrayList<>();
        userQuizList.add(new UserQuizDTO());
    }

    @Test
    void testGetAllUserQuizs() throws Exception {
        Mockito.when(userQuizService.getAllUserQuizzes()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/userQuizzes/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetUserQuizById() throws Exception {
        Long userQuizId = 1L;
        UserQuizDTO userQuiz = new UserQuizDTO();

        Mockito.when(userQuizService.getUserQuizById(userQuizId)).thenReturn(userQuiz);

        mockMvc.perform(get("/api/v1/userQuizzes/{id}", userQuizId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateUserQuiz() throws Exception {
        UserQuizDTO userQuiz = new UserQuizDTO();
        String userQuizJson = objectMapper.writeValueAsString(userQuiz);

        Mockito.when(userQuizService.createUserQuiz(Mockito.any(UserQuizDTO.class))).thenReturn(userQuiz);

        mockMvc.perform(post("/api/v1/userQuizzes/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userQuizJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateUserQuiz() throws Exception {
        Long userQuizId = 1L;
        UserQuizDTO updatedUserQuiz = new UserQuizDTO();
        String updatedUserQuizJson = objectMapper.writeValueAsString(updatedUserQuiz);

        Mockito.when(userQuizService.updateUserQuiz(Mockito.eq(userQuizId), Mockito.any(UserQuizDTO.class)))
                .thenReturn(updatedUserQuiz);

        mockMvc.perform(put("/api/v1/userQuizzes/{id}", userQuizId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserQuizJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteUserQuizById() throws Exception {
        Long userQuizId = 1L;
        mockMvc.perform(delete("/api/v1/userQuizzes/{id}", userQuizId))
                .andExpect(status().isOk());
    }
}

