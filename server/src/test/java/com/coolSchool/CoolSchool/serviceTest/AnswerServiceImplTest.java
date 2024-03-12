package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.exceptions.answer.AnswerNotFoundException;
import com.coolSchool.coolSchool.models.dto.common.AnswerDTO;
import com.coolSchool.coolSchool.models.entity.Answer;
import com.coolSchool.coolSchool.repositories.AnswerRepository;
import com.coolSchool.coolSchool.repositories.QuestionRepository;
import com.coolSchool.coolSchool.services.impl.AnswerServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnswerServiceImplTest {

    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private AnswerServiceImpl answerService;

    @Mock
    private QuestionRepository questionRepository;

    @Mock
    private MessageSource messageSource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        ModelMapper modelMapper = new ModelMapper();
        answerService = new AnswerServiceImpl(answerRepository, modelMapper, questionRepository, messageSource);
    }

    @Test
    void testDeleteAnswer_AnswerPresent() {
        Long answerId = 1L;
        Answer answer = new Answer();
        answer.setDeleted(false);
        Optional<Answer> answerOptional = Optional.of(answer);

        when(answerRepository.findByIdAndDeletedFalse(answerId)).thenReturn(answerOptional);

        assertDoesNotThrow(() -> answerService.deleteAnswer(answerId));

        Assertions.assertTrue(answer.isDeleted());
        verify(answerRepository, times(1)).save(answer);
    }

    @Test
    void testGetAllAnswers() {
        List<Answer> answers = Collections.singletonList(new Answer());
        when(answerRepository.findByDeletedFalse()).thenReturn(answers);

        List<AnswerDTO> result = answerService.getAllAnswers();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(answers.size(), result.size());
    }

    @Test
    void testGetAnswerById_AnswerPresent() {
        Long answerId = 1L;
        Answer answer = new Answer();
        Optional<Answer> answerOptional = Optional.of(answer);
        when(answerRepository.findByIdAndDeletedFalse(answerId)).thenReturn(answerOptional);

        AnswerDTO result = answerService.getAnswerById(answerId);

        Assertions.assertNotNull(result);
    }

    @Test
    void testGetAnswerById_AnswerNotPresent() {
        Long answerId = 1L;
        Optional<Answer> answerOptional = Optional.empty();
        when(answerRepository.findByIdAndDeletedFalse(answerId)).thenReturn(answerOptional);

        assertThrows(AnswerNotFoundException.class, () -> answerService.getAnswerById(answerId));
    }

    @Test
    void testUpdateAnswer_AnswerNotPresent() {
        Long answerId = 1L;
        AnswerDTO updatedAnswerDTO = new AnswerDTO();
        Optional<Answer> existingAnswerOptional = Optional.empty();
        when(answerRepository.findByIdAndDeletedFalse(answerId)).thenReturn(existingAnswerOptional);

        assertThrows(AnswerNotFoundException.class, () -> answerService.updateAnswer(answerId, updatedAnswerDTO));
    }
}