package com.coolSchool.CoolSchool.services.impl;

import com.coolSchool.CoolSchool.exceptions.common.NoSuchElementException;
import com.coolSchool.CoolSchool.exceptions.quizzes.QuizNotFoundException;
import com.coolSchool.CoolSchool.exceptions.quizzes.ValidationQuizException;
import com.coolSchool.CoolSchool.models.dto.*;
import com.coolSchool.CoolSchool.models.entity.Quiz;
import com.coolSchool.CoolSchool.repositories.CourseSubsectionRepository;
import com.coolSchool.CoolSchool.repositories.QuizRepository;
import com.coolSchool.CoolSchool.services.AnswerService;
import com.coolSchool.CoolSchool.services.QuestionService;
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
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final Validator validator;
    private final CourseSubsectionRepository courseSubsectionRepository;

    public QuizServiceImpl(QuizRepository quizRepository, ModelMapper modelMapper, QuestionService questionService, AnswerService answerService, Validator validator, CourseSubsectionRepository courseSubsectionRepository) {
        this.quizRepository = quizRepository;
        this.modelMapper = modelMapper;
        this.questionService = questionService;
        this.answerService = answerService;
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

    public QuizDTO createQuiz(QuizDataDTO quizData) {
        QuizDTO quizDTO = quizData.getQuizDTO();
        List<QuestionAndAnswersDTO> questionAndAnswersList = quizData.getData();
        Quiz savedQuiz = quizRepository.save(modelMapper.map(quizDTO, Quiz.class));

        for (QuestionAndAnswersDTO questionAndAnswers : questionAndAnswersList) {
            QuestionDTO questionDTO = questionAndAnswers.getQuestion();
            questionDTO.setQuizId(savedQuiz.getId());
            QuestionDTO savedQuestion = questionService.createQuestion(questionDTO);
            for (AnswerDTO answerDTO : questionAndAnswers.getAnswers()) {
                answerDTO.setQuestionId(savedQuestion.getId());
                answerService.createAnswer(answerDTO);
            }
        }
        return modelMapper.map(savedQuiz, QuizDTO.class);
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
