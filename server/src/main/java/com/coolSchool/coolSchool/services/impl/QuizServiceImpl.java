package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.exceptions.common.NoSuchElementException;
import com.coolSchool.coolSchool.exceptions.course.CourseNotFoundException;
import com.coolSchool.coolSchool.exceptions.courseSubsection.CourseSubsectionNotFoundException;
import com.coolSchool.coolSchool.exceptions.quizzes.NoMoreAttemptsQuizException;
import com.coolSchool.coolSchool.exceptions.quizzes.QuizNotFoundException;
import com.coolSchool.coolSchool.exceptions.quizzes.ValidationQuizException;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.*;
import com.coolSchool.coolSchool.models.entity.*;
import com.coolSchool.coolSchool.repositories.*;
import com.coolSchool.coolSchool.services.AnswerService;
import com.coolSchool.coolSchool.services.QuestionService;
import com.coolSchool.coolSchool.services.QuizService;
import com.coolSchool.coolSchool.services.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final ModelMapper modelMapper;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;
    private final UserAnswerRepository userAnswerRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final CourseSubsectionRepository courseSubsectionRepository;
    private final CourseRepository courseRepository;
    private final UserQuizProgressRepository userQuizProgressRepository;

    public QuizServiceImpl(QuizRepository quizRepository, ModelMapper modelMapper, QuestionService questionService, AnswerService answerService, UserService userService, UserAnswerRepository userAnswerRepository, QuizAttemptRepository quizAttemptRepository, CourseSubsectionRepository courseSubsectionRepository, CourseRepository courseRepository, UserQuizProgressRepository userQuizProgressRepository) {
        this.quizRepository = quizRepository;
        this.modelMapper = modelMapper;
        this.questionService = questionService;
        this.answerService = answerService;
        this.userService = userService;
        this.userAnswerRepository = userAnswerRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.courseSubsectionRepository = courseSubsectionRepository;
        this.courseRepository = courseRepository;
        this.userQuizProgressRepository = userQuizProgressRepository;
    }

    @Override
    public List<QuizDTO> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findByDeletedFalse();
        return quizzes.stream().map(quiz -> modelMapper.map(quiz, QuizDTO.class)).toList();
    }

    @Override
    public QuizQuestionsAnswersDTO getQuizById(Long id, Long userId) {
        Quiz quiz = quizRepository.findByIdAndDeletedFalse(id).orElseThrow(QuizNotFoundException::new);
        List<Question> questions = questionService.getQuestionsByQuizId(id);
        QuizDTO quizDTO = modelMapper.map(quiz, QuizDTO.class);

        AtomicReference<List<AnswerDTO>> filteredAnswers = new AtomicReference<>();

        boolean isUserTheCreatorOfQuiz = isTheUserQuizCreator(userId, quiz);

        List<QuestionAndAnswersDTO> questionAndAnswersList = questions.stream()
                .map(question -> {
                    List<AnswerDTO> answers = answerService.getAnswersByQuestionId(question.getId());

                    if (isUserTheCreatorOfQuiz) {
                        filteredAnswers.set(answers.stream()
                                .map(answer -> new AnswerDTO(answer.getId(), answer.getText(), answer.getQuestionId(), answer.isCorrect())).toList());
                    } else {
                        filteredAnswers.set(answers.stream()
                                .map(answer -> new AnswerDTO(answer.getId(), answer.getText(), answer.getQuestionId())).toList());
                    }

                    QuestionDTO questionDTO = modelMapper.map(question, QuestionDTO.class);
                    return new QuestionAndAnswersDTO(questionDTO, filteredAnswers.get());
                })
                .collect(Collectors.toList());

        List<UserQuizProgress> userQuizProgresses = userQuizProgressRepository.findByUserIdAndQuizId(userId, id);
        List<UserQuizProgressDTO> userQuizProgressDTOS = userQuizProgresses.stream()
                .map(userQuizProgress -> modelMapper.map(userQuizProgress, UserQuizProgressDTO.class)).toList();

        if (userQuizProgressDTOS.isEmpty()) {
            return new QuizQuestionsAnswersDTO(quizDTO, questionAndAnswersList);
        }
        return new QuizQuestionsAnswersDTO(quizDTO, questionAndAnswersList, userQuizProgressDTOS);
    }

    @Override
    public List<QuizDTO> getQuizzesBySubsectionId(Long subsectionId) {
        List<Quiz> quizzes = quizRepository.findBySubsectionIdAndDeletedFalse(subsectionId);
        return quizzes.stream()
                .map(quiz -> modelMapper.map(quiz, QuizDTO.class))
                .collect(Collectors.toList());
    }

    @Override
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

    @Override
    public QuizResultDTO takeQuiz(Long quizId, List<UserAnswerDTO> userAnswers, Long userId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(QuizNotFoundException::new);

        int attemptNumber = quizAttemptRepository.countByUserAndQuiz(userService.findById(userId), quiz) + 1;
        if (attemptNumber > quiz.getAttemptLimit()) {
            throw new NoMoreAttemptsQuizException();
        }

        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setQuiz(quiz);
        quizAttempt.setUser(userService.findById(userId));
        quizAttempt.setAttemptNumber(attemptNumber);
        quizAttempt = quizAttemptRepository.save(quizAttempt);

        BigDecimal totalMarks = BigDecimal.ZERO;

        for (UserAnswerDTO userAnswerDTO : userAnswers) {
            QuestionDTO question = questionService.getQuestionById(userAnswerDTO.getQuestionId());
            AnswerDTO answer = answerService.getAnswerById(userAnswerDTO.getSelectedOptionId());

            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setQuestion(modelMapper.map(question, Question.class));
            userAnswer.setAnswer(modelMapper.map(answer, Answer.class));
            userAnswer.setQuizAttempt(quizAttempt);

            userAnswerRepository.save(userAnswer);

            List<AnswerDTO> correctAnswers = answerService.getCorrectAnswersByQuestionId(question.getId());
            boolean isCorrect = isUserAnswerCorrect(userAnswerDTO, correctAnswers);

            if (isCorrect) {
                totalMarks = totalMarks.add(question.getMarks());
            }
        }

        quizAttempt.setTotalMarks(totalMarks);
        quizAttemptRepository.save(quizAttempt);

        return new QuizResultDTO(new QuizAttemptDTO(modelMapper.map(quiz, QuizDTO.class), modelMapper.map(quizAttempt.getUser(), PublicUserDTO.class),
                userAnswers, quizAttempt.getTotalMarks(), quizAttempt.getAttemptNumber()));

    }

    @Override
    public void autoSaveUserProgress(Long quizId, Long questionId, Long answerId, Long userId) {
        UserQuizProgressDTO userQuizProgressDTO = new UserQuizProgressDTO();
        userQuizProgressDTO.setUserId(userId);
        userQuizProgressDTO.setQuizId(quizId);
        userQuizProgressDTO.setAnswerId(answerId);
        userQuizProgressDTO.setQuestionId(questionId);

        UserQuizProgress userQuizProgress = modelMapper.map(userQuizProgressDTO, UserQuizProgress.class);
        userQuizProgressRepository.save(userQuizProgress);
    }

    @Override
    @Transactional
    public void deleteAutoSavedProgress(Long userId, Long quizId) {
        userQuizProgressRepository.deleteByUserIdAndQuizId(userId, quizId);
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

    private boolean isTheUserQuizCreator(Long userId, Quiz quiz) {
        CourseSubsection courseSubsection = courseSubsectionRepository.findByIdAndDeletedFalse(quiz.getSubsection().getId()).orElseThrow(CourseSubsectionNotFoundException::new);
        Course course = courseRepository.findByIdAndDeletedFalse(courseSubsection.getCourse().getId()).orElseThrow(CourseNotFoundException::new);
        return Objects.equals(course.getUser().getId(), userId);
    }
}
