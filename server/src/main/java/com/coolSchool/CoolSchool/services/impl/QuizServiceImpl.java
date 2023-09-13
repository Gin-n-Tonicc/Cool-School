package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.exceptions.common.NoSuchElementException;
import com.coolSchool.CoolSchool.exceptions.quizzes.QuizNotFoundException;
import com.coolSchool.CoolSchool.exceptions.quizzes.ValidationQuizException;
import com.coolSchool.CoolSchool.models.dto.QuizDTO;
import com.coolSchool.CoolSchool.models.entity.Quiz;
import com.coolSchool.CoolSchool.repositories.CourseSubsectionRepository;
import com.coolSchool.CoolSchool.repositories.QuizRepository;
import com.coolSchool.CoolSchool.services.QuizService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;
    private final CourseSubsectionRepository courseSubsectionRepository;

    public QuizServiceImpl(QuizRepository quizRepository, ModelMapper modelMapper, Validator validator, CourseSubsectionRepository courseSubsectionRepository) {
        this.quizRepository = quizRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
        this.courseSubsectionRepository = courseSubsectionRepository;
    }

    @Override
    public List<QuizDTO> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findByDeletedFalse();
        return quizzes.stream().map(quiz -> modelMapper.map(quiz, QuizDTO.class)).toList();
    }

    @Override
    public QuizDTO getQuizById(Long id) {
        Optional<Quiz> quiz = quizRepository.findByIdAndDeletedFalse(id);
        if (quiz.isPresent()) {
            return modelMapper.map(quiz.get(), QuizDTO.class);
        }
        throw new QuizNotFoundException();
    }

    @Override
    public List<QuizDTO> getQuizzesBySubsectionId(Long subsectionId) {
        List<Quiz> quizzes = quizRepository.findBySubsectionIdAndDeletedFalse(subsectionId);
        return quizzes.stream()
                .map(quiz -> modelMapper.map(quiz, QuizDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public QuizDTO createQuiz(QuizDTO quizDTO) {
        try {
            quizDTO.setId(null);
            courseSubsectionRepository.findByIdAndDeletedFalse(quizDTO.getSubsectionId()).orElseThrow(NoSuchElementException::new);
            Quiz quizEntity = quizRepository.save(modelMapper.map(quizDTO, Quiz.class));
            return modelMapper.map(quizEntity, QuizDTO.class);
        } catch (TransactionException exception) {
            if (exception.getRootCause() instanceof ConstraintViolationException validationException) {
                throw new ValidationQuizException(validationException.getConstraintViolations());
            }
            throw exception;
        }
    }

    @Override
    public QuizDTO updateQuiz(Long id, QuizDTO quizDTO) {
        Optional<Quiz> existingQuizOptional = quizRepository.findByIdAndDeletedFalse(id);

        if (existingQuizOptional.isEmpty()) {
            throw new QuizNotFoundException();
        }
        courseSubsectionRepository.findByIdAndDeletedFalse(quizDTO.getSubsectionId()).orElseThrow(NoSuchElementException::new);
        Quiz existingQuiz = existingQuizOptional.get();
        modelMapper.map(quizDTO, existingQuiz);

        try {
            existingQuiz.setId(id);
            Quiz updatedQuiz = quizRepository.save(existingQuiz);
            return modelMapper.map(updatedQuiz, QuizDTO.class);
        } catch (TransactionException exception) {
            if (exception.getRootCause() instanceof ConstraintViolationException validationException) {
                throw new ValidationQuizException(validationException.getConstraintViolations());
            }
            throw exception;
        }
    }

    @Override
    public void deleteQuiz(Long id) {
        Optional<Quiz> quiz = quizRepository.findByIdAndDeletedFalse(id);
        if (quiz.isPresent()) {
            quiz.get().setDeleted(true);
            quizRepository.save(quiz.get());
        } else {
            throw new QuizNotFoundException();
        }
    }
}
