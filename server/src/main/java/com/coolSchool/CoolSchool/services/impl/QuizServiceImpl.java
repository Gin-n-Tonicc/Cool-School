package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.exceptions.quizzes.QuizNotFoundException;
import com.coolSchool.CoolSchool.exceptions.quizzes.ValidationQuizException;
import com.coolSchool.CoolSchool.models.dto.QuizDTO;
import com.coolSchool.CoolSchool.models.entity.Quiz;
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

    public QuizServiceImpl(QuizRepository quizRepository, ModelMapper modelMapper, Validator validator) {
        this.quizRepository = quizRepository;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    public List<QuizDTO> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findByDeletedFalse();
        return quizzes.stream().map(quiz -> modelMapper.map(quiz, QuizDTO.class)).toList();
    }

    public QuizDTO getQuizById(Long id) {
        Optional<Quiz> quiz = quizRepository.findByIdAndDeletedFalse(id);
        if (quiz.isPresent()) {
            return modelMapper.map(quiz.get(), QuizDTO.class);
        }
        throw new QuizNotFoundException();
    }

    public List<QuizDTO> getQuizzesBySubsectionId(Long subsectionId) {
        List<Quiz> quizzes = quizRepository.findBySubsectionIdAndDeletedFalse(subsectionId);
        return quizzes.stream()
                .map(quiz -> modelMapper.map(quiz, QuizDTO.class))
                .collect(Collectors.toList());
    }

    public QuizDTO createQuiz(QuizDTO quizDTO) {
        try {
            quizRepository.save(modelMapper.map(quizDTO, Quiz.class));
            return quizDTO;
        } catch (ConstraintViolationException exception) {
            throw new ValidationQuizException(exception.getConstraintViolations());
        }
    }

    public QuizDTO updateQuiz(Long id, QuizDTO quizDTO) {
        Optional<Quiz> existingQuizOptional = quizRepository.findByIdAndDeletedFalse(id);

        if (existingQuizOptional.isEmpty()) {
            throw new QuizNotFoundException();
        }

        Quiz existingQuiz = existingQuizOptional.get();
        modelMapper.map(quizDTO, existingQuiz);

        try {
            Quiz updatedQuiz = quizRepository.save(existingQuiz);
            return modelMapper.map(updatedQuiz, QuizDTO.class);
        } catch (TransactionException exception) {
            if (exception.getRootCause() instanceof ConstraintViolationException validationException) {
                throw new ValidationQuizException(validationException.getConstraintViolations());
            }

            throw exception;
        }
    }

    public void deleteQuiz(Long id) {
        Optional<Quiz> quiz = quizRepository.findByIdAndDeletedFalse(id);
        if (quiz.isPresent()) {
            quiz.get().setDeleted(true);
            quizRepository.save(quiz.get());
        }
        throw new QuizNotFoundException();

    }
}
