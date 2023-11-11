package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.CoolSchool.controllers.QuizController;
import com.coolSchool.CoolSchool.models.dto.common.QuizDTO;
import com.coolSchool.CoolSchool.services.impl.QuizServiceImpl;
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
@WebMvcTest(value = QuizController.class,
        useDefaultFilters = false,
        includeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        value = QuizController.class),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE
                )
        }
)
class QuizControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private QuizServiceImpl quizService;
    private List<QuizDTO> quizList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
        quizList = new ArrayList<>();
        quizList.add(new QuizDTO());
    }

    @Test
    void testGetAllQuizzes() throws Exception {
        Mockito.when(quizService.getAllQuizzes()).thenReturn(Collections.emptyList());
        mockMvc.perform(get("/api/v1/quizzes/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetQuizById() throws Exception {
        Long quizId = 1L;
        QuizDTO quiz = new QuizDTO();

        Mockito.when(quizService.getQuizById(quizId)).thenReturn(quiz);

        mockMvc.perform(get("/api/v1/quizzes/{id}", quizId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetQuizzesBySubsectionId() throws Exception {
        Long subsectionId = 1L;

        Mockito.when(quizService.getQuizzesBySubsectionId(subsectionId)).thenReturn(quizList);

        mockMvc.perform(get("/api/v1/quizzes/subsection/{id}", subsectionId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    @Test
    void testUpdateQuiz() throws Exception {
        Long quizId = 1L;
        QuizDTO updatedQuiz = new QuizDTO();
        String updatedQuizJson = objectMapper.writeValueAsString(updatedQuiz);

        Mockito.when(quizService.updateQuiz(Mockito.eq(quizId), Mockito.any(QuizDTO.class)))
                .thenReturn(updatedQuiz);

        mockMvc.perform(put("/api/v1/quizzes/{id}", quizId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedQuizJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testDeleteQuizById() throws Exception {
        Long quizId = 1L;
        mockMvc.perform(delete("/api/v1/quizzes/{id}", quizId))
                .andExpect(status().isOk());
    }
}

