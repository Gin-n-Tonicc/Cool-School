package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.CoolSchool.exceptions.questions.QuestionNotFoundException;
import com.coolSchool.CoolSchool.exceptions.questions.ValidationQuestionException;
import com.coolSchool.CoolSchool.models.dto.QuestionDTO;
import com.coolSchool.CoolSchool.models.entity.Question;
import com.coolSchool.CoolSchool.models.entity.Quiz;
import com.coolSchool.CoolSchool.repositories.QuestionRepository;
import com.coolSchool.CoolSchool.repositories.QuizRepository;
import com.coolSchool.CoolSchool.services.impl.QuestionServiceImpl;
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
class QuestionServiceImplTest {

    @Mock
    private QuestionRepository questionRepository;
    @Mock
    private QuizRepository quizRepository;

    @InjectMocks
    private QuestionServiceImpl questionService;

    private ModelMapper modelMapper;
    private Validator validator;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        questionService = new QuestionServiceImpl(questionRepository, modelMapper, quizRepository, validator);
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
    void testGetAllQuestions() {
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
        when(questionRepository.save(any(Question.class))).thenReturn(question);
        when(quizRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Quiz()));
        QuestionDTO result = questionService.createQuestion(questionDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateQuestion() {
        Long questionId = 1L;
        QuestionDTO updatedQuestionDTO = new QuestionDTO();
        Question existingQuestion = new Question();
        Optional<Question> existingQuestionOptional = Optional.of(existingQuestion);
        when(questionRepository.findByIdAndDeletedFalse(questionId)).thenReturn(existingQuestionOptional);
        when(questionRepository.save(any(Question.class))).thenReturn(existingQuestion);
        when(quizRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Quiz()));
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

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(questionRepository.save(any(Question.class))).thenThrow(constraintViolationException);
        when(quizRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Quiz()));
        assertThrows(ValidationQuestionException.class, () -> questionService.createQuestion(questionDTO));
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

        when(questionRepository.findByIdAndDeletedFalse(questionId)).thenReturn(existingQuestionOptional);
        when(questionRepository.save(any(Question.class))).thenThrow(constraintViolationException);
        when(quizRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Quiz()));
        assertThrows(ConstraintViolationException.class, () -> questionService.updateQuestion(questionId, questionDTO));
    }
}

