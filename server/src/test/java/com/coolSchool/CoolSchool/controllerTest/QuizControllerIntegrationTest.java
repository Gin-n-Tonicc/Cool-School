package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.QuizController;
import com.coolSchool.coolSchool.exceptions.answer.filters.JwtAuthenticationFilter;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.*;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

    @Test
    void testCreateQuiz() {
        QuizDataDTO quizDataDTO = new QuizDataDTO();
        QuizDTO createdQuizDTO = new QuizDTO();

        when(quizService.createQuiz(quizDataDTO)).thenReturn(createdQuizDTO);
        ResponseEntity<QuizDTO> responseEntity = quizController.createQuiz(quizDataDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(createdQuizDTO, responseEntity.getBody());
    }

    @Test
    void testTakeQuiz() {
        Long quizId = 1L;
        Long userId = 1L;
        QuizAttemptDTO quizAttemptDTO = new QuizAttemptDTO();

        when(quizService.takeQuiz(quizId, userId)).thenReturn(quizAttemptDTO);

        HttpServletRequest request = mock(HttpServletRequest.class);

        PublicUserDTO publicUserDTO = new PublicUserDTO();
        publicUserDTO.setId(1L);

        when(request.getAttribute(JwtAuthenticationFilter.userKey)).thenReturn(publicUserDTO);

        ResponseEntity<QuizAttemptDTO> responseEntity = quizController.takeQuiz(quizId, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testSubmitQuiz() {
        Long quizId = 1L;
        Long attemptId = 1L;
        Long userId = 1L;

        List<UserAnswerDTO> userAnswers = Collections.emptyList();
        QuizResultDTO quizResultDTO = new QuizResultDTO();

        when(quizService.submitQuiz(quizId, userAnswers, userId, attemptId)).thenReturn(quizResultDTO);

        HttpServletRequest request = mock(HttpServletRequest.class);

        PublicUserDTO publicUserDTO = new PublicUserDTO();
        publicUserDTO.setId(1L);
        when(request.getAttribute(JwtAuthenticationFilter.userKey)).thenReturn(publicUserDTO);

        ResponseEntity<QuizResultDTO> responseEntity = quizController.submitQuiz(quizId, attemptId, userAnswers, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(quizResultDTO, responseEntity.getBody());
    }

    @Test
    void testGetQuizById() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        PublicUserDTO publicUserDTO = new PublicUserDTO();
        publicUserDTO.setId(1L);
        when(request.getAttribute(JwtAuthenticationFilter.userKey)).thenReturn(publicUserDTO);

        QuizService quizService = mock(QuizService.class);
        QuizQuestionsAnswersDTO quizQuestionsAnswersDTO = new QuizQuestionsAnswersDTO();
        when(quizService.getQuizById(1L, publicUserDTO.getId())).thenReturn(quizQuestionsAnswersDTO);

        QuizController quizController = new QuizController(quizService);
        ResponseEntity<QuizQuestionsAnswersDTO> responseEntity = quizController.getQuizById(1L, request);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(quizQuestionsAnswersDTO, responseEntity.getBody());
    }

    @Test
    void testUpdateQuiz() {
        QuizService quizService = mock(QuizService.class);
        QuizDataDTO updatedQuizData = new QuizDataDTO();
        QuizDTO updatedQuiz = new QuizDTO();
        when(quizService.updateQuiz(1L, updatedQuizData)).thenReturn(updatedQuiz);

        QuizController quizController = new QuizController(quizService);
        ResponseEntity<QuizDTO> responseEntity = quizController.updateQuiz(1L, updatedQuizData);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedQuiz, responseEntity.getBody());
    }

    @Test
    void testDeleteQuizById() {
        QuizService quizService = mock(QuizService.class);
        doNothing().when(quizService).deleteQuiz(1L);

        QuizController quizController = new QuizController(quizService);
        ResponseEntity<String> responseEntity = quizController.deleteQuizById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Quiz with id: 1 has been deleted successfully!", responseEntity.getBody());
    }

    @Test
    void testGetQuizAttemptDetails() {
        QuizService quizService = mock(QuizService.class);
        QuizAttemptDTO quizAttemptDTO = new QuizAttemptDTO();
        when(quizService.getQuizAttemptDetails(1L)).thenReturn(quizAttemptDTO);

        QuizController quizController = new QuizController(quizService);
        ResponseEntity<QuizAttemptDTO> responseEntity = quizController.getQuizAttemptDetails(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(quizAttemptDTO, responseEntity.getBody());
    }
}
