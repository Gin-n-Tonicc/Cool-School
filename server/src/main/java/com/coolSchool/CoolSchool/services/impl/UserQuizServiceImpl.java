package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.exceptions.userQuiz.NoMoreAttemptsForUserQuiz;
import com.coolSchool.CoolSchool.exceptions.userQuiz.UserQuizNotFoundException;
import com.coolSchool.CoolSchool.exceptions.userQuiz.ValidationUserQuizException;
import com.coolSchool.CoolSchool.models.dto.UserQuizDTO;
import com.coolSchool.CoolSchool.models.entity.*;
import com.coolSchool.CoolSchool.repositories.QuizRepository;
import com.coolSchool.CoolSchool.repositories.UserAnswerRepository;
import com.coolSchool.CoolSchool.repositories.UserQuizRepository;
import com.coolSchool.CoolSchool.repositories.UserRepository;
import com.coolSchool.CoolSchool.services.UserQuizService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserQuizServiceImpl implements UserQuizService {
    private final UserQuizRepository userQuizRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;
    private final UserAnswerRepository userAnswerRepository;
    private final UserRepository userRepository;
    private final QuizRepository quizRepository;

    public UserQuizServiceImpl(UserQuizRepository userQuizRepository, ModelMapper modelMapper, Validator validator, UserAnswerRepository userAnswerRepository, UserRepository userRepository, QuizRepository quizRepository) {
        this.userQuizRepository = userQuizRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
        this.userAnswerRepository = userAnswerRepository;
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
    }

    @Override
    public List<UserQuizDTO> getAllUserQuizzes() {
        List<UserQuiz> userQuizs = userQuizRepository.findByDeletedFalse();
        return userQuizs.stream().map(userQuiz -> modelMapper.map(userQuiz, UserQuizDTO.class)).toList();
    }

    @Override
    public UserQuizDTO getUserQuizById(Long id) {
        Optional<UserQuiz> userQuiz = userQuizRepository.findByIdAndDeletedFalse(id);
        if (userQuiz.isPresent()) {
            return modelMapper.map(userQuiz.get(), UserQuizDTO.class);
        }
        throw new UserQuizNotFoundException();
    }

    @Override
    public UserQuizDTO createUserQuiz(UserQuizDTO userQuizDTO) {
        try {
            UserQuiz userQuizEntity = userQuizRepository.save(modelMapper.map(userQuizDTO, UserQuiz.class));
            return modelMapper.map(userQuizEntity, UserQuizDTO.class);
        } catch (ConstraintViolationException exception) {
            throw new ValidationUserQuizException(exception.getConstraintViolations());
        }
    }

    @Override
    public UserQuizDTO updateUserQuiz(Long id, UserQuizDTO userQuizDTO) {
        Optional<UserQuiz> existingUserQuizOptional = userQuizRepository.findByIdAndDeletedFalse(id);

        if (existingUserQuizOptional.isEmpty()) {
            throw new UserQuizNotFoundException();
        }

        UserQuiz existingUserQuiz = existingUserQuizOptional.get();
        modelMapper.map(userQuizDTO, existingUserQuiz);

        try {
            existingUserQuiz.setId(id);
            UserQuiz updatedUserQuiz = userQuizRepository.save(existingUserQuiz);
            return modelMapper.map(updatedUserQuiz, UserQuizDTO.class);
        } catch (TransactionException exception) {
            if (exception.getRootCause() instanceof ConstraintViolationException validationException) {
                throw new ValidationUserQuizException(validationException.getConstraintViolations());
            }
            throw exception;
        }
    }

    @Override
    public void deleteUserQuiz(Long id) {
        Optional<UserQuiz> userQuiz = userQuizRepository.findByIdAndDeletedFalse(id);
        if (userQuiz.isPresent()) {
            userQuiz.get().setDeleted(true);
            userQuizRepository.save(userQuiz.get());
        } else {
            throw new UserQuizNotFoundException();
        }
    }

    @Override
    public List<UserQuizDTO> calculateUserTotalMarks(Long userId, Long quizId) {
        Optional<User> user = userRepository.findByIdAndDeletedFalse(userId);
        Optional<Quiz> quiz = quizRepository.findByIdAndDeletedFalse(quizId);

        if (user.isPresent() && quiz.isPresent()) {
            List<UserQuiz> userQuizzes = userQuizRepository.findByUserAndQuiz(user.get(), quiz.get());
            userQuizzes.forEach(this::calculateTotalMarksForUserQuiz);
            return userQuizzes.stream().map(userQuiz -> modelMapper.map(userQuiz, UserQuizDTO.class)).toList();
        } else {
            throw new UserQuizNotFoundException();
        }
    }

    private void calculateTotalMarksForUserQuiz(UserQuiz userQuiz) {
        BigDecimal totalMarks = calculateTotalMarksForQuizAttempt(userQuiz);
        userQuiz.setTotalMarks(totalMarks);
        userQuizRepository.save(userQuiz);
    }

    public BigDecimal calculateTotalMarksForQuizAttempt(UserQuiz userQuiz) {
        List<UserAnswer> userAnswers = getUserAnswersForQuizAttempt(userQuiz);
        BigDecimal totalMarks = BigDecimal.ZERO;

        for (UserAnswer userAnswer : userAnswers) {
            Answer answer = userAnswer.getAnswer();
            if (answer != null && answer.isCorrect() && Objects.equals(userAnswer.getAttemptNumber(), userQuiz.getAttemptNumber())) {
                totalMarks = totalMarks.add(answer.getQuestionId().getMarks());
            }
        }
        return totalMarks;
    }

    public List<UserAnswer> getUserAnswersForQuizAttempt(UserQuiz userQuiz) {
        return userAnswerRepository.findByUserAndAttemptNumber(userQuiz.getUser(), userQuiz.getAttemptNumber());
    }

    public Integer calculateTheNextAttemptNumber(Long userId, Long quizId) {
        Optional<Integer> maxAttemptNumber;
        List<UserQuiz> userQuizzes = userQuizRepository.findByUserAndQuiz(userRepository.findByIdAndDeletedFalse(userId).get(), quizRepository.findByIdAndDeletedFalse(quizId).get());
        maxAttemptNumber = userQuizzes.stream()
                .map(UserQuiz::getAttemptNumber)
                .max(Integer::compareTo);
        if (maxAttemptNumber.map(num -> num + 1).get() > quizRepository.findByIdAndDeletedFalse(quizId).get().getAttemptLimit()) {
            throw new NoMoreAttemptsForUserQuiz();
        }
        return maxAttemptNumber.map(num -> num + 1).orElse(1);
    }
}

