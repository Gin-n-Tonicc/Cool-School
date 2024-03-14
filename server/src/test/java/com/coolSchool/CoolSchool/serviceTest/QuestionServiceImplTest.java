package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.exceptions.questions.QuestionNotFoundException;
import com.coolSchool.coolSchool.models.dto.common.QuestionDTO;
import com.coolSchool.coolSchool.models.entity.Question;
import com.coolSchool.coolSchool.models.entity.Quiz;
import com.coolSchool.coolSchool.repositories.QuestionRepository;
import com.coolSchool.coolSchool.repositories.QuizRepository;
import com.coolSchool.coolSchool.services.impl.QuestionServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuestionServiceImplTest {

    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private QuizRepository quizRepository;
    @InjectMocks
    private QuestionServiceImpl questionService;

    private ModelMapper modelMapper;
    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        questionService = new QuestionServiceImpl(questionRepository, modelMapper, quizRepository, messageSource);
    }

    @Test
    public void testDeleteQuestion_QuestionPresent() {
        Long questionId = 1L;

        Question question = new Question();
        question.setDeleted(false);

        Optional<Question> questionOptional = Optional.of(question);

        when(questionRepository.findByIdAndDeletedFalse(questionId)).thenReturn(questionOptional);
        when(questionRepository.save(any(Question.class))).thenReturn(question);

        assertDoesNotThrow(() -> questionService.deleteQuestion(questionId));
        assertTrue(question.isDeleted());
        verify(questionRepository, times(1)).save(question);
    }

    @Test
    void testGetAllCategories() {
        List<Question> questionList = new ArrayList<>();
        questionList.add(new Question());
        Mockito.when(questionRepository.findByDeletedFalse()).thenReturn(questionList);
        List<QuestionDTO> result = questionService.getAllQuestions();
        assertNotNull(result);
        assertEquals(questionList.size(), result.size());
    }

    @Test
    void testGetQuestionById() {
        Long questionId = 1L;
        Question question = new Question();
        Optional<Question> questionOptional = Optional.of(question);
        when(questionRepository.findByIdAndDeletedFalse(questionId)).thenReturn(questionOptional);
        QuestionDTO result = questionService.getQuestionById(questionId);
        assertNotNull(result);
    }

    @Test
    void testGetQuestionByIdNotFound() {
        Long questionId = 1L;
        Optional<Question> questionOptional = Optional.empty();
        when(questionRepository.findByIdAndDeletedFalse(questionId)).thenReturn(questionOptional);
        assertThrows(QuestionNotFoundException.class, () -> questionService.getQuestionById(questionId));
    }

    @Test
    void testCreateQuestion() {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = modelMapper.map(questionDTO, Question.class);

        when(quizRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Quiz()));
        when(questionRepository.save(any(Question.class))).thenReturn(question);
        QuestionDTO result = questionService.createQuestion(questionDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateQuestion() {
        Long questionId = 1L;
        QuestionDTO updatedQuestionDTO = new QuestionDTO();
        Question existingQuestion = new Question();
        Optional<Question> existingQuestionOptional = Optional.of(existingQuestion);

        when(quizRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Quiz()));
        when(questionRepository.findByIdAndDeletedFalse(questionId)).thenReturn(existingQuestionOptional);
        when(questionRepository.save(any(Question.class))).thenReturn(existingQuestion);
        QuestionDTO result = questionService.updateQuestion(questionId, updatedQuestionDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateQuestionNotFound() {
        Long nonExistentQuestionId = 99L;
        QuestionDTO updatedQuestionDTO = new QuestionDTO();
        when(questionRepository.findByIdAndDeletedFalse(nonExistentQuestionId)).thenReturn(Optional.empty());
        assertThrows(QuestionNotFoundException.class, () -> questionService.updateQuestion(nonExistentQuestionId, updatedQuestionDTO));
    }

    @Test
    void testDeleteQuestionNotFound() {
        Long nonExistentQuestionId = 99L;

        when(questionRepository.findByIdAndDeletedFalse(nonExistentQuestionId)).thenReturn(Optional.empty());

        assertThrows(QuestionNotFoundException.class, () -> questionService.deleteQuestion(nonExistentQuestionId));
    }

    @Test
    void testCreateQuestion_ValidationException() {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setDescription(null);
        questionDTO.setMarks(null);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(quizRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Quiz()));
        when(questionRepository.save(any(Question.class))).thenThrow(constraintViolationException);

        assertThrows(ConstraintViolationException.class, () -> questionService.createQuestion(questionDTO));
    }

    @Test
    void testUpdateQuestion_ValidationException() {
        Long questionId = 1L;
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setDescription(null);
        Question existingQuestion = new Question();
        Optional<Question> existingQuestionOptional = Optional.of(existingQuestion);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(quizRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Quiz()));
        when(questionRepository.findByIdAndDeletedFalse(questionId)).thenReturn(existingQuestionOptional);
        when(questionRepository.save(any(Question.class))).thenThrow(constraintViolationException);

        assertThrows(ConstraintViolationException.class, () -> questionService.updateQuestion(questionId, questionDTO));
    }

    @Test
    void testCreateQuestion_DataIntegrityViolationException() {
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(1L);
        questionDTO.setDescription("Test Question");

        when(quizRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Quiz()));

        when(questionRepository.save(any(Question.class))).thenThrow(QuestionNotFoundException.class);

        assertThrows(QuestionNotFoundException.class, () -> questionService.createQuestion(questionDTO));
    }

    @Test
    void testGetQuestionsByQuizId() {
        Long quizId = 1L;
        List<Question> expectedQuestions = new ArrayList<>();
        expectedQuestions.add(new Question());
        when(questionRepository.findByQuizId(quizId)).thenReturn(expectedQuestions);

        List<Question> result = questionService.getQuestionsByQuizId(quizId);

        assertNotNull(result);
        assertEquals(expectedQuestions.size(), result.size());
    }
}