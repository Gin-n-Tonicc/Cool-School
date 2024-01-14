package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import com.coolSchool.coolSchool.exceptions.quizzes.QuizNotFoundException;
import com.coolSchool.coolSchool.exceptions.quizzes.ValidationQuizException;
import com.coolSchool.coolSchool.models.dto.common.*;
import com.coolSchool.coolSchool.models.entity.Question;
import com.coolSchool.coolSchool.models.entity.Quiz;
import com.coolSchool.coolSchool.repositories.CourseSubsectionRepository;
import com.coolSchool.coolSchool.repositories.QuizRepository;
import com.coolSchool.coolSchool.services.AnswerService;
import com.coolSchool.coolSchool.services.QuestionService;
import com.coolSchool.coolSchool.services.QuizService;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    public QuizQuestionsAnswersDTO getQuizById(Long id) {
        Optional<Quiz> quizOptional = quizRepository.findByIdAndDeletedFalse(id);

        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();

            List<Question> questions = questionService.getQuestionsByQuizId(id);
            QuizDTO quizDTO = modelMapper.map(quiz, QuizDTO.class);
            List<QuestionAndAnswersDTO> questionAndAnswersList = questions.stream()
                    .map(question -> {
                        List<AnswerDTO> answers = answerService.getAnswersByQuestionId(question.getId());
                        QuestionDTO questionDTO = modelMapper.map(question, QuestionDTO.class);
                        return new QuestionAndAnswersDTO(questionDTO, answers);
                    })
                    .collect(Collectors.toList());

            return new QuizQuestionsAnswersDTO(quizDTO, questionAndAnswersList);
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

    public QuizResultDTO takeQuiz(Long quizId, List<UserAnswerDTO> userAnswers) {
        Optional<Quiz> quizOptional = quizRepository.findById(quizId);

        if (quizOptional.isPresent()) {
            Quiz quiz = quizOptional.get();
            List<QuestionAndAnswersDTO> questionAndAnswersList = new ArrayList<>();
            BigDecimal totalMarks = BigDecimal.ZERO;

            List<Question> questions = questionService.getQuestionsByQuizId(quizId);

            for (Question question : questions) {

                List<AnswerDTO> correctAnswers = answerService.getCorrectAnswersByQuestionId(question.getId());
                UserAnswerDTO userAnswer = findUserAnswer(userAnswers, question.getId());

                boolean isCorrect = isUserAnswerCorrect(userAnswer, correctAnswers);

                if (isCorrect) {
                    totalMarks = totalMarks.add(question.getMarks());
                }

                QuestionDTO questionDTO = modelMapper.map(question, QuestionDTO.class);
                QuestionAndAnswersDTO questionAndAnswersDTO = new QuestionAndAnswersDTO(questionDTO, correctAnswers);
                questionAndAnswersList.add(questionAndAnswersDTO);
            }
            return new QuizResultDTO(quiz.getId(), quiz.getTitle(), totalMarks, questionAndAnswersList);
        } else {
            throw new QuizNotFoundException();
        }
    }

    private UserAnswerDTO findUserAnswer(List<UserAnswerDTO> userAnswers, Long questionId) {
        for (UserAnswerDTO userAnswer : userAnswers) {
            if (userAnswer.getQuestionId().equals(questionId)) {
                return userAnswer;
            }
        }
        return null;
    }

    private boolean isUserAnswerCorrect(UserAnswerDTO userAnswer, List<AnswerDTO> correctAnswers) {
        if (userAnswer == null) {
            return false;
        }
        Long userSelectedOptionId = userAnswer.getSelectedOptionId();
        if (userSelectedOptionId == null) {
            return false;
        }
        return correctAnswers.stream()
                .anyMatch(correctAnswer -> correctAnswer.getId().equals(userSelectedOptionId));
    }
}
