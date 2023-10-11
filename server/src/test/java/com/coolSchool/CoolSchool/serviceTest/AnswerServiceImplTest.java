package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.CoolSchool.exceptions.answer.AnswerNotFoundException;
import com.coolSchool.CoolSchool.exceptions.answer.ValidationAnswerException;
import com.coolSchool.CoolSchool.models.dto.AnswerDTO;
import com.coolSchool.CoolSchool.models.entity.Answer;
import com.coolSchool.CoolSchool.models.entity.Question;
import com.coolSchool.CoolSchool.repositories.AnswerRepository;
import com.coolSchool.CoolSchool.repositories.QuestionRepository;
import com.coolSchool.CoolSchool.services.impl.AnswerServiceImpl;
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
class AnswerServiceImplTest {

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private AnswerServiceImpl answerService;

    private ModelMapper modelMapper;
    private Validator validator;
    @Mock
    private QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        answerService = new AnswerServiceImpl(answerRepository, modelMapper, questionRepository, validator);
    }

    @Test
    public void testDeleteAnswer_AnswerPresent() {
        Long answerId = 1L;

        Answer answer = new Answer();
        answer.setDeleted(false);

        Optional<Answer> answerOptional = Optional.of(answer);

        when(answerRepository.findByIdAndDeletedFalse(answerId)).thenReturn(answerOptional);
        when(answerRepository.save(any(Answer.class))).thenReturn(answer);

        assertDoesNotThrow(() -> answerService.deleteAnswer(answerId));
        assertTrue(answer.isDeleted());
        verify(answerRepository, times(1)).save(answer);
    }

    @Test
    void testGetAllAnswerzes() {
        List<Answer> answerList = new ArrayList<>();
        answerList.add(new Answer());
        Mockito.when(answerRepository.findByDeletedFalse()).thenReturn(answerList);
        List<AnswerDTO> result = answerService.getAllAnswers();
        assertNotNull(result);
        assertEquals(answerList.size(), result.size());
    }

    @Test
    void testGetAnswerById() {
        Long answerId = 1L;
        Answer answer = new Answer();
        Optional<Answer> answerOptional = Optional.of(answer);
        when(answerRepository.findByIdAndDeletedFalse(answerId)).thenReturn(answerOptional);
        AnswerDTO result = answerService.getAnswerById(answerId);
        assertNotNull(result);
    }

    @Test
    void testGetAnswerByIdNotFound() {
        Long answerId = 1L;
        Optional<Answer> answerOptional = Optional.empty();
        when(answerRepository.findByIdAndDeletedFalse(answerId)).thenReturn(answerOptional);
        assertThrows(AnswerNotFoundException.class, () -> answerService.getAnswerById(answerId));
    }

    @Test
    void testCreateAnswer() {
        AnswerDTO answerDTO = new AnswerDTO();
        Answer answer = modelMapper.map(answerDTO, Answer.class);
        when(answerRepository.save(any(Answer.class))).thenReturn(answer);
        when(questionRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Question()));
        AnswerDTO result = answerService.createAnswer(answerDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateAnswer() {
        Long answerId = 1L;
        AnswerDTO updatedAnswerDTO = new AnswerDTO();
        Answer existingAnswer = new Answer();
        Optional<Answer> existingAnswerOptional = Optional.of(existingAnswer);
        when(answerRepository.findByIdAndDeletedFalse(answerId)).thenReturn(existingAnswerOptional);
        when(answerRepository.save(any(Answer.class))).thenReturn(existingAnswer);
        when(questionRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Question()));
        AnswerDTO result = answerService.updateAnswer(answerId, updatedAnswerDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateAnswerNotFound() {
        Long nonExistentAnswerId = 99L;
        AnswerDTO updatedAnswerDTO = new AnswerDTO();
        when(answerRepository.findByIdAndDeletedFalse(nonExistentAnswerId)).thenReturn(Optional.empty());
        assertThrows(AnswerNotFoundException.class, () -> answerService.updateAnswer(nonExistentAnswerId, updatedAnswerDTO));
    }

    @Test
    void testDeleteAnswerNotFound() {
        Long nonExistentAnswerId = 99L;

        when(answerRepository.findByIdAndDeletedFalse(nonExistentAnswerId)).thenReturn(Optional.empty());

        assertThrows(AnswerNotFoundException.class, () -> answerService.deleteAnswer(nonExistentAnswerId));
    }

    @Test
    void testCreateAnswer_ValidationException() {
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setText(null);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(answerRepository.save(any(Answer.class))).thenThrow(constraintViolationException);
        when(questionRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Question()));
        assertThrows(ValidationAnswerException.class, () -> answerService.createAnswer(answerDTO));
    }

    @Test
    void testUpdateAnswer_ValidationException() {
        Long answerId = 1L;
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setText(null);
        Answer existingAnswer = new Answer();
        Optional<Answer> existingAnswerOptional = Optional.of(existingAnswer);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);
        when(questionRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Question()));
        when(answerRepository.findByIdAndDeletedFalse(answerId)).thenReturn(existingAnswerOptional);
        when(answerRepository.save(any(Answer.class))).thenThrow(constraintViolationException);

        assertThrows(ConstraintViolationException.class, () -> answerService.updateAnswer(answerId, answerDTO));
    }
}

