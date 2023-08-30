package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.CoolSchool.controllers.AnswerController;
import com.coolSchool.CoolSchool.models.dto.AnswerDTO;
import com.coolSchool.CoolSchool.services.impl.AnswerServiceImpl;
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
@WebMvcTest(value = AnswerController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = AnswerController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
class AnswerControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AnswerServiceImpl answerService;
    private List<AnswerDTO> answerList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        answerList = new ArrayList<>();
        answerList.add(new AnswerDTO());
    }

    @Test
    void testGetAllAnswers() throws Exception {
        Mockito.when(answerService.getAllAnswers()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/answers/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetAnswerById() throws Exception {
        Long answerId = 1L;
        AnswerDTO answer = new AnswerDTO();

        Mockito.when(answerService.getAnswerById(answerId)).thenReturn(answer);

        mockMvc.perform(get("/api/v1/answers/{id}", answerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateAnswer() throws Exception {
        AnswerDTO answer = new AnswerDTO();
        String answerJson = objectMapper.writeValueAsString(answer);

        Mockito.when(answerService.createAnswer(Mockito.any(AnswerDTO.class))).thenReturn(answer);

        mockMvc.perform(post("/api/v1/answers/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(answerJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateAnswer() throws Exception {
        Long answerId = 1L;
        AnswerDTO updatedAnswer = new AnswerDTO();
        String updatedAnswerJson = objectMapper.writeValueAsString(updatedAnswer);

        Mockito.when(answerService.updateAnswer(Mockito.eq(answerId), Mockito.any(AnswerDTO.class)))
                .thenReturn(updatedAnswer);

        mockMvc.perform(put("/api/v1/answers/{id}", answerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedAnswerJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteAnswerById() throws Exception {
        Long answerId = 1L;
        mockMvc.perform(delete("/api/v1/answers/{id}", answerId))
                .andExpect(status().isOk());
    }
}

