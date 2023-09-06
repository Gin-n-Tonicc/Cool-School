package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.CoolSchool.controllers.QuestionController;
import com.coolSchool.CoolSchool.models.dto.QuestionDTO;
import com.coolSchool.CoolSchool.services.impl.QuestionServiceImpl;
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
@WebMvcTest(value = QuestionController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = QuestionController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
class QuestionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private QuestionServiceImpl questionService;
    private List<QuestionDTO> questionList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        questionList = new ArrayList<>();
        questionList.add(new QuestionDTO());
    }

    @Test
    void testGetAllQuestions() throws Exception {
        Mockito.when(questionService.getAllQuestions()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/questions/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetQuestionById() throws Exception {
        Long questionId = 1L;
        QuestionDTO question = new QuestionDTO();

        Mockito.when(questionService.getQuestionById(questionId)).thenReturn(question);

        mockMvc.perform(get("/api/v1/questions/{id}", questionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testCreateQuestion() throws Exception {
        QuestionDTO question = new QuestionDTO();
        String questionJson = objectMapper.writeValueAsString(question);

        Mockito.when(questionService.createQuestion(Mockito.any(QuestionDTO.class))).thenReturn(question);

        mockMvc.perform(post("/api/v1/questions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(questionJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testUpdateQuestion() throws Exception {
        Long questionId = 1L;
        QuestionDTO updatedQuestion = new QuestionDTO();
        String updatedQuestionJson = objectMapper.writeValueAsString(updatedQuestion);

        Mockito.when(questionService.updateQuestion(Mockito.eq(questionId), Mockito.any(QuestionDTO.class)))
                .thenReturn(updatedQuestion);

        mockMvc.perform(put("/api/v1/questions/{id}", questionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedQuestionJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteQuestionById() throws Exception {
        Long questionId = 1L;
        mockMvc.perform(delete("/api/v1/questions/{id}", questionId))
                .andExpect(status().isOk());
    }
}
