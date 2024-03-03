package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.QuizController;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.QuizDTO;
import com.coolSchool.coolSchool.models.dto.common.QuizQuestionsAnswersDTO;
import com.coolSchool.coolSchool.services.QuizService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QuizControllerIntegrationTest {

    @Mock
    private QuizService quizService;

    @InjectMocks
    private QuizController quizController;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetAllQuizzes() {
        List<QuizDTO> quizzes = new ArrayList<>();
        quizzes.add(new QuizDTO());
        quizzes.add(new QuizDTO());
        quizzes.get(0).setId(1L);
        quizzes.get(1).setId(2L);

        when(quizService.getAllQuizzes()).thenReturn(quizzes);

        ResponseEntity<List<QuizDTO>> response = quizController.getAllQuizzes();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(quizzes, response.getBody());
    }

    @Test
    void testGetQuizInfoById() {

        QuizDTO quiz = new QuizDTO();
        quiz.setId(1L);

        when(quizService.getQuizInfoById(1L)).thenReturn(quiz);

        ResponseEntity<QuizDTO> response = quizController.getQuizInfoById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(quiz, response.getBody());
    }

    @Test
    void testGetQuizzesBySubsectionId() {
        List<QuizDTO> quizzes = new ArrayList<>();
        quizzes.add(new QuizDTO());
        quizzes.add(new QuizDTO());
        quizzes.get(0).setId(1L);
        quizzes.get(1).setId(2L);

        when(quizService.getQuizzesBySubsectionId(1L)).thenReturn(quizzes);

        ResponseEntity<List<QuizDTO>> response = quizController.getQuizzesBySubsectionId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(quizzes, response.getBody());
    }
}
