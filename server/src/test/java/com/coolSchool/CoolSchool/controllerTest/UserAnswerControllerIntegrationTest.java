package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.CoolSchool.controllers.UserAnswerController;
import com.coolSchool.CoolSchool.models.dto.UserAnswerDTO;
import com.coolSchool.CoolSchool.services.impl.UserAnswerServiceImpl;
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
@WebMvcTest(value = UserAnswerController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = UserAnswerController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
class UserAnswerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserAnswerServiceImpl userAnswerService;
    private List<UserAnswerDTO> userAnswerList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        userAnswerList = new ArrayList<>();
        userAnswerList.add(new UserAnswerDTO());
    }

    @Test
    void testGetAllUserAnswers() throws Exception {
        Mockito.when(userAnswerService.getAllUserAnswers()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/userAnswers/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetUserAnswerById() throws Exception {
        Long userAnswerId = 1L;
        UserAnswerDTO userAnswer = new UserAnswerDTO();

        Mockito.when(userAnswerService.getUserAnswerById(userAnswerId)).thenReturn(userAnswer);

        mockMvc.perform(get("/api/v1/userAnswers/{id}", userAnswerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateUserAnswer() throws Exception {
        UserAnswerDTO userAnswer = new UserAnswerDTO();
        String userAnswerJson = objectMapper.writeValueAsString(userAnswer);

        Mockito.when(userAnswerService.createUserAnswer(Mockito.any(UserAnswerDTO.class))).thenReturn(userAnswer);

        mockMvc.perform(post("/api/v1/userAnswers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userAnswerJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateUserAnswer() throws Exception {
        Long userAnswerId = 1L;
        UserAnswerDTO updatedUserAnswer = new UserAnswerDTO();
        String updatedUserAnswerJson = objectMapper.writeValueAsString(updatedUserAnswer);

        Mockito.when(userAnswerService.updateUserAnswer(Mockito.eq(userAnswerId), Mockito.any(UserAnswerDTO.class)))
                .thenReturn(updatedUserAnswer);

        mockMvc.perform(put("/api/v1/userAnswers/{id}", userAnswerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedUserAnswerJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteUserAnswerById() throws Exception {
        Long userAnswerId = 1L;
        mockMvc.perform(delete("/api/v1/userAnswers/{id}", userAnswerId))
                .andExpect(status().isOk());
    }
}


