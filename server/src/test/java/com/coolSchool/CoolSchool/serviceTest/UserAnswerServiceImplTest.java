package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.CoolSchool.exceptions.userAnswer.UserAnswerNotFoundException;
import com.coolSchool.CoolSchool.exceptions.userAnswer.ValidationUserAnswerException;
import com.coolSchool.CoolSchool.models.dto.UserAnswerDTO;
import com.coolSchool.CoolSchool.models.entity.Answer;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.models.entity.UserAnswer;
import com.coolSchool.CoolSchool.repositories.AnswerRepository;
import com.coolSchool.CoolSchool.repositories.UserAnswerRepository;
import com.coolSchool.CoolSchool.repositories.UserRepository;
import com.coolSchool.CoolSchool.services.impl.UserAnswerServiceImpl;
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
class UserAnswerServiceImplTest {

    @Mock
    private UserAnswerRepository userAnswerRepository;

    @InjectMocks
    private UserAnswerServiceImpl userAnswerService;

    private ModelMapper modelMapper;
    private Validator validator;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AnswerRepository answerRepository;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        userAnswerService = new UserAnswerServiceImpl(userAnswerRepository, modelMapper, answerRepository, userRepository, validator);
    }

    @Test
    public void testDeleteUserAnswer_UserAnswerPresent() {
        Long userAnswerId = 1L;

        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setDeleted(false);

        Optional<UserAnswer> userAnswerOptional = Optional.of(userAnswer);

        when(userAnswerRepository.findByIdAndDeletedFalse(userAnswerId)).thenReturn(userAnswerOptional);
        when(userAnswerRepository.save(any(UserAnswer.class))).thenReturn(userAnswer);

        assertDoesNotThrow(() -> userAnswerService.deleteUserAnswer(userAnswerId));
        assertTrue(userAnswer.isDeleted());
        verify(userAnswerRepository, times(1)).save(userAnswer);
    }

    @Test
    void testGetAllUserAnswers() {
        List<UserAnswer> userAnswerList = new ArrayList<>();
        userAnswerList.add(new UserAnswer());
        Mockito.when(userAnswerRepository.findByDeletedFalse()).thenReturn(userAnswerList);
        List<UserAnswerDTO> result = userAnswerService.getAllUserAnswers();
        assertNotNull(result);
        assertEquals(userAnswerList.size(), result.size());
    }

    @Test
    void testGetUserAnswerById() {
        Long userAnswerId = 1L;
        UserAnswer userAnswer = new UserAnswer();
        Optional<UserAnswer> userAnswerOptional = Optional.of(userAnswer);
        when(userAnswerRepository.findByIdAndDeletedFalse(userAnswerId)).thenReturn(userAnswerOptional);
        UserAnswerDTO result = userAnswerService.getUserAnswerById(userAnswerId);
        assertNotNull(result);
    }

    @Test
    void testGetUserAnswerByIdNotFound() {
        Long userAnswerId = 1L;
        Optional<UserAnswer> userAnswerOptional = Optional.empty();
        when(userAnswerRepository.findByIdAndDeletedFalse(userAnswerId)).thenReturn(userAnswerOptional);
        assertThrows(UserAnswerNotFoundException.class, () -> userAnswerService.getUserAnswerById(userAnswerId));
    }

    @Test
    void testCreateUserAnswer() {
        UserAnswerDTO userAnswerDTO = new UserAnswerDTO();
        UserAnswer userAnswer = modelMapper.map(userAnswerDTO, UserAnswer.class);
        when(answerRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Answer()));
        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new User()));
        when(userAnswerRepository.save(any(UserAnswer.class))).thenReturn(userAnswer);
        UserAnswerDTO result = userAnswerService.createUserAnswer(userAnswerDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateUserAnswer() {
        Long userAnswerId = 1L;
        UserAnswerDTO updatedUserAnswerDTO = new UserAnswerDTO();
        UserAnswer existingUserAnswer = new UserAnswer();

        Optional<UserAnswer> existingUserAnswerOptional = Optional.of(existingUserAnswer);
        when(userAnswerRepository.findByIdAndDeletedFalse(userAnswerId)).thenReturn(existingUserAnswerOptional);
        when(userAnswerRepository.save(any(UserAnswer.class))).thenReturn(existingUserAnswer);
        UserAnswerDTO result = userAnswerService.updateUserAnswer(userAnswerId, updatedUserAnswerDTO);
        assertNotNull(result);
    }

    @Test
    void testUpdateUserAnswerNotFound() {
        Long nonExistentUserAnswerId = 99L;
        UserAnswerDTO updatedUserAnswerDTO = new UserAnswerDTO();
        when(userAnswerRepository.findByIdAndDeletedFalse(nonExistentUserAnswerId)).thenReturn(Optional.empty());
        assertThrows(UserAnswerNotFoundException.class, () -> userAnswerService.updateUserAnswer(nonExistentUserAnswerId, updatedUserAnswerDTO));
    }

    @Test
    void testDeleteUserAnswerNotFound() {
        Long nonExistentUserAnswerId = 99L;

        when(userAnswerRepository.findByIdAndDeletedFalse(nonExistentUserAnswerId)).thenReturn(Optional.empty());

        assertThrows(UserAnswerNotFoundException.class, () -> userAnswerService.deleteUserAnswer(nonExistentUserAnswerId));
    }

    @Test
    void testCreateUserAnswer_ValidationException() {
        UserAnswerDTO userAnswerDTO = new UserAnswerDTO();
        userAnswerDTO.setUserId(null);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(userAnswerRepository.save(any(UserAnswer.class))).thenThrow(constraintViolationException);
        when(answerRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new Answer()));
        when(userRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new User()));
        assertThrows(ValidationUserAnswerException.class, () -> userAnswerService.createUserAnswer(userAnswerDTO));
    }

    @Test
    void testUpdateUserAnswer_ValidationException() {
        Long userAnswerId = 1L;
        UserAnswerDTO userAnswerDTO = new UserAnswerDTO();
        userAnswerDTO.setAnswerId(null);
        UserAnswer existingUserAnswer = new UserAnswer();
        Optional<UserAnswer> existingUserAnswerOptional = Optional.of(existingUserAnswer);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(userAnswerRepository.findByIdAndDeletedFalse(userAnswerId)).thenReturn(existingUserAnswerOptional);
        when(userAnswerRepository.save(any(UserAnswer.class))).thenThrow(constraintViolationException);

        assertThrows(ConstraintViolationException.class, () -> userAnswerService.updateUserAnswer(userAnswerId, userAnswerDTO));
    }
}

