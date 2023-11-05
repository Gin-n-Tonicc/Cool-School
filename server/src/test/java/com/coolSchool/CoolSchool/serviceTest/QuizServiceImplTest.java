package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.CoolSchool.exceptions.quizzes.QuizNotFoundException;
import com.coolSchool.CoolSchool.models.dto.QuizDTO;
import com.coolSchool.CoolSchool.models.dto.QuizDataDTO;
import com.coolSchool.CoolSchool.models.entity.CourseSubsection;
import com.coolSchool.CoolSchool.models.entity.Quiz;
import com.coolSchool.CoolSchool.repositories.CourseSubsectionRepository;
import com.coolSchool.CoolSchool.repositories.QuizRepository;
import com.coolSchool.CoolSchool.services.AnswerService;
import com.coolSchool.CoolSchool.services.QuestionService;
import com.coolSchool.CoolSchool.services.impl.QuizServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest {

    @Mock
    private QuizRepository quizRepository;
    @Mock
    private CourseSubsectionRepository courseSubsectionRepository;
    @Mock
    private QuestionService questionService;
    @Mock
    private AnswerService answerService;

    @InjectMocks
    private QuizServiceImpl quizService;

    private ModelMapper modelMapper;
    private Validator validator;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        quizService = new QuizServiceImpl(quizRepository, modelMapper,questionService, answerService, validator, courseSubsectionRepository);
    }

    @Test
    public void testDeleteQuiz_QuizPresent() {
        Long quizId = 1L;

        Quiz quiz = new Quiz();
        quiz.setDeleted(false);

        Optional<Quiz> quizOptional = Optional.of(quiz);

        when(quizRepository.findByIdAndDeletedFalse(quizId)).thenReturn(quizOptional);
        when(quizRepository.save(any(Quiz.class))).thenReturn(quiz);

        assertDoesNotThrow(() -> quizService.deleteQuiz(quizId));
        assertTrue(quiz.isDeleted());
        verify(quizRepository, times(1)).save(quiz);
    }

    @Test
    void testGetAllQuizzes() {
        List<Quiz> quizList = new ArrayList<>();
        quizList.add(new Quiz());
        Mockito.when(quizRepository.findByDeletedFalse()).thenReturn(quizList);
        List<QuizDTO> result = quizService.getAllQuizzes();
        assertNotNull(result);
        assertEquals(quizList.size(), result.size());
    }

    @Test
    void testGetQuizById() {
        Long quizId = 1L;
        Quiz quiz = new Quiz();
        Optional<Quiz> quizOptional = Optional.of(quiz);
        when(quizRepository.findByIdAndDeletedFalse(quizId)).thenReturn(quizOptional);
        QuizDTO result = quizService.getQuizById(quizId);
        assertNotNull(result);
    }

    @Test
    void testGetQuizByIdNotFound() {
        Long quizId = 1L;
        Optional<Quiz> quizOptional = Optional.empty();
        when(quizRepository.findByIdAndDeletedFalse(quizId)).thenReturn(quizOptional);
        assertThrows(QuizNotFoundException.class, () -> quizService.getQuizById(quizId));
    }


    @Test
    void testUpdateQuiz() {
        Long quizId = 1L;
        QuizDTO updatedQuizDTO = new QuizDTO();
        Quiz existingQuiz = new Quiz();
        Optional<Quiz> existingQuizOptional = Optional.of(existingQuiz);
        when(quizRepository.findByIdAndDeletedFalse(quizId)).thenReturn(existingQuizOptional);
        when(quizRepository.save(any(Quiz.class))).thenReturn(existingQuiz);
        when(courseSubsectionRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new CourseSubsection()));
        QuizDTO result = quizService.updateQuiz(quizId, updatedQuizDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateQuizNotFound() {
        Long nonExistentQuizId = 99L;
        QuizDTO updatedQuizDTO = new QuizDTO();
        when(quizRepository.findByIdAndDeletedFalse(nonExistentQuizId)).thenReturn(Optional.empty());
        assertThrows(QuizNotFoundException.class, () -> quizService.updateQuiz(nonExistentQuizId, updatedQuizDTO));
    }

    @Test
    void testDeleteQuizNotFound() {
        Long nonExistentQuizId = 99L;

        when(quizRepository.findByIdAndDeletedFalse(nonExistentQuizId)).thenReturn(Optional.empty());

        assertThrows(QuizNotFoundException.class, () -> quizService.deleteQuiz(nonExistentQuizId));
    }


    @Test
    void testGetQuizzesBySubsectionId() {
        Long subsectionId = 1L;
        Quiz quiz1 = new Quiz();
        quiz1.setId(1L);
        CourseSubsection courseSubsection = new CourseSubsection();
        courseSubsection.setId(1L);
        quiz1.setSubsection(courseSubsection);
        Quiz quiz2 = new Quiz();
        quiz2.setId(2L);
        quiz2.setSubsection(courseSubsection);
        List<Quiz> quizzes = new ArrayList<>();
        quizzes.add(quiz1);
        quizzes.add(quiz2);
        when(quizRepository.findBySubsectionIdAndDeletedFalse(subsectionId)).thenReturn(quizzes);
        List<QuizDTO> quizDTOs = quizService.getQuizzesBySubsectionId(subsectionId);
        assertEquals(2, quizDTOs.size());
        assertEquals(quiz1.getId(), quizDTOs.get(0).getSubsectionId());
    }

    @Test
    void testUpdateQuiz_ValidationException() {
        Long quizId = 1L;
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setTitle(null);
        Quiz existingQuiz = new Quiz();
        Optional<Quiz> existingQuizOptional = Optional.of(existingQuiz);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(quizRepository.findByIdAndDeletedFalse(quizId)).thenReturn(existingQuizOptional);
        when(quizRepository.save(any(Quiz.class))).thenThrow(constraintViolationException);
        when(courseSubsectionRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new CourseSubsection()));
        assertThrows(ConstraintViolationException.class, () -> quizService.updateQuiz(quizId, quizDTO));
    }
}
