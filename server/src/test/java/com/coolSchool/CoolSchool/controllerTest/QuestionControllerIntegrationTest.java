package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.QuestionController;
import com.coolSchool.coolSchool.models.dto.common.QuestionDTO;
import com.coolSchool.coolSchool.services.QuestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionControllerIntegrationTest {

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private QuestionController questionController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetAllQuestions() {
        List<QuestionDTO> questions = Arrays.asList(
                new QuestionDTO(),
                new QuestionDTO()
        );
        questions.get(0).setId(1L);
        questions.get(0).setDescription("Question 1");
        questions.get(1).setId(2L);
        questions.get(1).setDescription("Question 2");

        when(questionService.getAllQuestions()).thenReturn(questions);

        ResponseEntity<List<QuestionDTO>> response = questionController.getAllQuestions();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(questions, response.getBody());
    }

    @Test
    void testGetQuestionById() {
        QuestionDTO question = new QuestionDTO();
        question.setId(1L);
        question.setDescription("Test question");

        when(questionService.getQuestionById(1L)).thenReturn(question);

        ResponseEntity<QuestionDTO> response = questionController.getQuestionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(question, response.getBody());
    }

    @Test
    void testCreateQuestion() {
        QuestionDTO question = new QuestionDTO();
        question.setId(1L);
        question.setDescription("Test question");

        when(questionService.createQuestion(question)).thenReturn(question);

        ResponseEntity<QuestionDTO> response = questionController.createQuestion(question);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(question, response.getBody());
    }

    @Test
    void testUpdateQuestion() {
        QuestionDTO question = new QuestionDTO();
        question.setId(1L);
        question.setDescription("Test question");

        when(questionService.updateQuestion(1L, question)).thenReturn(question);

        ResponseEntity<QuestionDTO> response = questionController.updateQuestion(1L, question);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(question, response.getBody());
    }

    @Test
    void testDeleteQuestionById() {
        Long questionId = 1L;

        ResponseEntity<String> response = questionController.deleteQuestionById(questionId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Question with id: " + questionId + " has been deleted successfully!", response.getBody());
        verify(questionService, times(1)).deleteQuestion(questionId);
    }
}

