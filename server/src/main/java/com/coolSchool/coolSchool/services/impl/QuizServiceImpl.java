package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.config.schedulers.QuizAttemptTimer;
import com.coolSchool.coolSchool.exceptions.course.CourseNotFoundException;
import com.coolSchool.coolSchool.exceptions.courseSubsection.CourseSubsectionNotFoundException;
import com.coolSchool.coolSchool.exceptions.quizzes.*;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.*;
import com.coolSchool.coolSchool.models.dto.response.CourseResponseDTO;
import com.coolSchool.coolSchool.models.dto.response.UserCourseResponseDTO;
import com.coolSchool.coolSchool.models.entity.*;
import com.coolSchool.coolSchool.repositories.*;
import com.coolSchool.coolSchool.services.AnswerService;
import com.coolSchool.coolSchool.services.QuestionService;
import com.coolSchool.coolSchool.services.QuizService;
import com.coolSchool.coolSchool.services.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
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
    private final MessageSource messageSource;
    private final QuizAttemptTimer quizAttemptTimer;
    private final UserCourseRepository userCourseRepository;

    public QuizServiceImpl(QuizRepository quizRepository, ModelMapper modelMapper, QuestionService questionService, AnswerService answerService, UserService userService, UserAnswerRepository userAnswerRepository, QuizAttemptRepository quizAttemptRepository, CourseSubsectionRepository courseSubsectionRepository, CourseRepository courseRepository, UserQuizProgressRepository userQuizProgressRepository, MessageSource messageSource, QuizAttemptTimer quizAttemptTimer, UserCourseRepository userCourseRepository) {
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
        this.messageSource = messageSource;
        this.quizAttemptTimer = quizAttemptTimer;
        this.userCourseRepository = userCourseRepository;
    }

    @Override
    public List<QuizDTO> getAllQuizzes() {
        List<Quiz> quizzes = quizRepository.findByDeletedFalse();
        return quizzes.stream().map(quiz -> modelMapper.map(quiz, QuizDTO.class)).toList();
    }

    @Override
    public QuizDTO getQuizInfoById(Long id) {
        Quiz quiz = quizRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new QuizNotFoundException(messageSource));
        QuizDTO quizDTO = modelMapper.map(quiz, QuizDTO.class);
        quizDTO.setCourseId(quiz.getSubsection().getCourse().getId());

        return quizDTO;
    }

    /**
     * Retrieves a quiz with questions and answers by its ID.
     *
     * @param id     The ID of the quiz.
     * @param userId The ID of the user.
     * @return The DTO containing quiz details, questions, and answers.
     * @throws QuizNotFoundException If the quiz is not found.
     */
    @Override
    public QuizQuestionsAnswersDTO getQuizById(Long id, Long userId) {
        Quiz quiz = quizRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new QuizNotFoundException(messageSource));
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

    /**
     * Retrieves a list of quizzes by subsection ID.
     *
     * @param subsectionId The ID of the subsection.
     * @return A list of DTOs containing quiz details.
     */
    @Override
    public List<QuizDTO> getQuizzesBySubsectionId(Long subsectionId) {
        List<Quiz> quizzes = quizRepository.findBySubsectionIdAndDeletedFalse(subsectionId);
        return quizzes.stream()
                .map(quiz -> {
                    QuizDTO quizDTO = modelMapper.map(quiz, QuizDTO.class);
                    quizDTO.setCourseId(quiz.getSubsection().getCourse().getId());

                    return quizDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * Creates a new quiz with the provided quiz data.
     *
     * @param quizData The DTO containing quiz details and associated questions with answers.
     * @return The DTO representing the created quiz.
     * @throws MultipleCorrectAnswersException if multiple correct answers are found for a single question.
     */
    @Override
    public QuizDTO createQuiz(QuizDataDTO quizData) {
        QuizDTO quizDTO = quizData.getQuizDTO();
        List<QuestionAndAnswersDTO> questionAndAnswersList = quizData.getData();
        Quiz savedQuiz = quizRepository.save(modelMapper.map(quizDTO, Quiz.class));

        BigDecimal quizTotalMarks = BigDecimal.valueOf(0);

        for (QuestionAndAnswersDTO questionAndAnswers : questionAndAnswersList) {
            QuestionDTO questionDTO = questionAndAnswers.getQuestion();
            questionDTO.setQuizId(savedQuiz.getId());
            QuestionDTO savedQuestion = questionService.createQuestion(questionDTO);

            boolean foundCorrectAnswer = false;

            for (AnswerDTO answerDTO : questionAndAnswers.getAnswers()) {
                answerDTO.setQuestionId(savedQuestion.getId());
                if (answerDTO.isCorrect()) {
                    if (foundCorrectAnswer) {
                        throw new MultipleCorrectAnswersException(messageSource);
                    } else {
                        foundCorrectAnswer = true;
                    }
                }
                answerService.createAnswer(answerDTO);
            }
            quizTotalMarks = quizTotalMarks.add(savedQuestion.getMarks());
        }

        savedQuiz.setTotalMarks(quizTotalMarks);
        quizRepository.save(savedQuiz);

        return modelMapper.map(savedQuiz, QuizDTO.class);
    }

    /**
     * Updates an existing quiz with the provided quiz data.
     *
     * @param quizId          The ID of the quiz to be updated.
     * @param updatedQuizData The DTO containing updated quiz details and associated questions with answers.
     * @return The DTO representing the updated quiz.
     * @throws QuizNotFoundException             if the quiz with the specified ID is not found.
     * @throws CourseSubsectionNotFoundException if the associated course subsection is not found.
     */
    @Override
    public QuizDTO updateQuiz(Long quizId, QuizDataDTO updatedQuizData) {
        QuizDTO updatedQuizDTO = updatedQuizData.getQuizDTO();
        List<QuestionAndAnswersDTO> updatedQuestionAndAnswersList = updatedQuizData.getData();

        Quiz existingQuiz = quizRepository.findById(quizId).orElseThrow(() -> new QuizNotFoundException(messageSource));

        CourseSubsection courseSubsection = courseSubsectionRepository.findByIdAndDeletedFalse(updatedQuizDTO.getSubsectionId()).orElseThrow(() -> new CourseSubsectionNotFoundException(messageSource));

        existingQuiz.setTitle(updatedQuizDTO.getTitle());
        existingQuiz.setDescription(updatedQuizDTO.getDescription());
        existingQuiz.setStartTime(updatedQuizDTO.getStartTime());
        existingQuiz.setEndTime(updatedQuizDTO.getEndTime());
        existingQuiz.setSubsection(courseSubsection);
        existingQuiz.setAttemptLimit(updatedQuizDTO.getAttemptLimit());

        Quiz savedQuiz = quizRepository.save(existingQuiz);

        for (QuestionAndAnswersDTO updatedQnA : updatedQuestionAndAnswersList) {
            QuestionDTO updatedQuestionDTO = updatedQnA.getQuestion();
            updatedQuestionDTO.setQuizId(savedQuiz.getId());
            updatedQuestionDTO.setDescription(updatedQnA.getQuestion().getDescription());
            updatedQuestionDTO.setMarks(updatedQnA.getQuestion().getMarks());

            QuestionDTO savedQuestion = questionService.updateQuestion(updatedQuestionDTO.getId(), updatedQuestionDTO);

            for (AnswerDTO updatedAnswerDTO : updatedQnA.getAnswers()) {
                updatedAnswerDTO.setQuestionId(savedQuestion.getId());
                updatedAnswerDTO.setCorrect(updatedAnswerDTO.isCorrect());
                updatedAnswerDTO.setText(updatedAnswerDTO.getText());
                answerService.updateAnswer(updatedAnswerDTO.getId(), updatedAnswerDTO);
            }
        }
        return modelMapper.map(savedQuiz, QuizDTO.class);
    }

    /**
     * Deletes a quiz along with its associated questions, answers, and attempts.
     *
     * @param id The ID of the quiz to delete.
     * @throws QuizNotFoundException if the quiz with the specified ID is not found.
     */
    @Override
    public void deleteQuiz(Long id) {
        Quiz quiz = quizRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new QuizNotFoundException(messageSource));
        List<Question> quizQuestions = questionService.getQuestionsByQuizId(id);

        for (Question question : quizQuestions) {
            for (AnswerDTO answerDTO : answerService.getAnswersByQuestionId(question.getId())) {
                Answer answer = modelMapper.map(answerDTO, Answer.class);
                answer.setDeleted(true);
                answerService.deleteAnswer(answer.getId());
            }
            question.setDeleted(true);
            questionService.deleteQuestion(question.getId());
        }

        quiz.setDeleted(true);
        quizRepository.save(quiz);
    }

    /**
     * Initiates a quiz attempt for a user, validates quiz availability, time, and attempt limit.
     *
     * @param quizId The ID of the quiz to be attempted.
     * @param userId The ID of the user attempting the quiz.
     * @return The DTO representing the quiz attempt.
     * @throws QuizNotFoundException             if the quiz with the specified ID is not found.
     * @throws NoMoreAttemptsQuizException       if the user has exceeded the attempt limit for the quiz.
     * @throws QuizTimeNotValidException         if the current time is outside the quiz availability window.
     * @throws TimeLimitForQuizExceededException if the time limit for the quiz has exceeded.
     */
    @Override
    public QuizAttemptDTO takeQuiz(Long quizId, Long userId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new QuizNotFoundException(messageSource));

        int quizDurationInMinutes = quiz.getQuizDurationInMinutes();
        LocalDateTime startTime = LocalDateTime.now();

        int attemptNumber = quizAttemptRepository.countByUserAndQuiz(userService.findById(userId), quiz) + 1;
        if (attemptNumber > quiz.getAttemptLimit()) {
            throw new NoMoreAttemptsQuizException(messageSource);
        }

        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isBefore(quiz.getStartTime()) || currentTime.isAfter(quiz.getEndTime())) {
            throw new QuizTimeNotValidException(messageSource);
        }

        QuizAttempt quizAttempt = new QuizAttempt();

        LocalDateTime endTime = LocalDateTime.now();
        long elapsedTimeInMinutes = Duration.between(startTime, endTime).toMinutes();
        if (elapsedTimeInMinutes > quizDurationInMinutes) {
            quizAttempt.setCompleted(true);
            throw new TimeLimitForQuizExceededException(messageSource);
        }

        quizAttempt.setQuiz(quiz);
        quizAttempt.setUser(userService.findById(userId));
        quizAttempt.setAttemptNumber(attemptNumber);
        quizAttempt.setStartTime(LocalDateTime.now());
        quizAttempt = quizAttemptRepository.save(quizAttempt);
        quizAttempt.setTotalMarks(BigDecimal.ZERO);
        quizAttempt.setTimeLeft(quiz.getQuizDurationInMinutes());
        quizAttemptRepository.save(quizAttempt);

        return modelMapper.map(quizAttempt, QuizAttemptDTO.class);
    }

    /**
     * Submits a quiz attempt with user answers, calculates total marks, and marks the attempt as completed.
     *
     * @param quizId      The ID of the quiz being attempted.
     * @param userAnswers The list of user answers submitted for the quiz.
     * @param userId      The ID of the user submitting the quiz.
     * @param attemptId   The ID of the quiz attempt.
     * @return The DTO representing the result of the quiz attempt.
     * @throws QuizNotFoundException        if the quiz with the specified ID is not found.
     * @throws QuizTimeNotValidException    if the current time is outside the quiz availability window.
     * @throws QuizAttemptNotFoundException if the quiz attempt with the specified ID is not found.
     */
    @Override
    public QuizResultDTO submitQuiz(Long quizId, List<UserAnswerDTO> userAnswers, Long userId, Long attemptId) {
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new QuizNotFoundException(messageSource));

        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.isBefore(quiz.getStartTime()) || currentTime.isAfter(quiz.getEndTime())) {
            throw new QuizTimeNotValidException(messageSource);
        }

        QuizAttempt quizAttempt = quizAttemptRepository.findById(attemptId).orElseThrow(() -> new QuizAttemptNotFoundException(messageSource));
        quizAttempt.setQuiz(quiz);
        quizAttempt.setUser(userService.findById(userId));

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
        quizAttempt.setCompleted(true);
        quizAttempt.setQuizCompletionTime(LocalDateTime.now());
        QuizAttempt savedQuizAttempt = quizAttemptRepository.save(quizAttempt);
        return new QuizResultDTO(new QuizAttemptDTO(savedQuizAttempt.getId(), modelMapper.map(quiz, QuizDTO.class),
                userAnswers, savedQuizAttempt.getTotalMarks(), savedQuizAttempt.getAttemptNumber(), 0L, 0L, savedQuizAttempt.isCompleted()));
    }

    /**
     * Retrieves details of a quiz attempt including the time left.
     *
     * @param quizAttemptId The ID of the quiz attempt.
     * @return The DTO representing the details of the quiz attempt.
     * @throws QuizAttemptNotFoundException if the quiz attempt with the specified ID is not found.
     */
    @Override
    public QuizAttemptDTO getQuizAttemptDetails(Long quizAttemptId) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId)
                .orElseThrow(() -> new QuizAttemptNotFoundException(messageSource));

        long timeLeft = calculateTimeLeftForQuizAttempt(quizAttempt.getId(), quizAttempt.getQuiz().getQuizDurationInMinutes());

        QuizAttemptDTO quizAttemptDTO = modelMapper.map(quizAttempt, QuizAttemptDTO.class);
        quizAttemptDTO.setTimeLeft(timeLeft);

        return quizAttemptDTO;
    }

    /**
     * Retrieves all quiz attempts made by a user for a particular quiz.
     *
     * @param quizId        The ID of the quiz.
     * @param publicUserDTO The DTO representing the public user.
     * @return The list of DTOs representing the quiz attempts.
     */
    @Override
    public List<QuizAttemptDTO> getAllUserAttemptsInAQuiz(Long quizId, PublicUserDTO publicUserDTO) {
        List<QuizAttempt> quizAttempts = quizAttemptRepository.findByQuizIdAndUserId(quizId, publicUserDTO.getId());

        return quizAttempts.stream()
                .map(quizAttempt -> modelMapper.map(quizAttempt, QuizAttemptDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Automatically saves the user's progress during a quiz attempt.
     *
     * @param quizId        The ID of the quiz.
     * @param questionId    The ID of the question.
     * @param answerId      The ID of the selected answer.
     * @param userId        The ID of the user.
     * @param quizAttemptId The ID of the quiz attempt.
     * @return The list of DTOs representing the user's quiz progress.
     * @throws QuizAttemptNotFoundException      if the quiz attempt with the specified ID is not found.
     * @throws TimeLimitForQuizExceededException if the time limit for the quiz has been exceeded.
     */
    @Override
    @Transactional
    public List<UserQuizProgressDTO> autoSaveUserProgress(Long quizId, Long questionId, Long answerId, Long userId, Long quizAttemptId) {
        UserQuizProgressDTO userQuizProgressDTO = new UserQuizProgressDTO();

        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId).orElseThrow(() -> new QuizAttemptNotFoundException(messageSource));
        Quiz quiz = quizRepository.findById(quizId).orElseThrow(() -> new QuizNotFoundException(messageSource));

        userQuizProgressDTO.setUserId(userId);
        userQuizProgressDTO.setQuizId(quizId);
        userQuizProgressDTO.setAnswerId(answerId);
        userQuizProgressDTO.setQuestionId(questionId);
        quizAttempt.setTimeLeft(quiz.getQuizDurationInMinutes());
        quizAttemptTimer.updateQuizAttemptsTimeLeft();

        if (quizAttempt.getTimeLeft() <= 0) {
            throw new TimeLimitForQuizExceededException(messageSource);
        }
        calculateRemainingTimeInSeconds(quizAttempt);
        UserQuizProgress userQuizProgress = modelMapper.map(userQuizProgressDTO, UserQuizProgress.class);
        userQuizProgressRepository.deleteByUserIdAndQuizIdAndQuestionId(userId, quizId, questionId);
        userQuizProgressRepository.save(userQuizProgress);

        return getAllUserProgressForQuiz(quizId);
    }

    /**
     * Calculates the remaining time in seconds for a quiz attempt.
     *
     * @param quizAttempt The quiz attempt for which to calculate the remaining time.
     */
    public void calculateRemainingTimeInSeconds(QuizAttempt quizAttempt) {
        Quiz quiz = quizAttempt.getQuiz();

        if (quiz != null && quizAttempt.getStartTime() != null && quiz.getQuizDurationInMinutes() != null) {
            int quizDurationInSeconds = quiz.getQuizDurationInMinutes() * 60;
            LocalDateTime quizEndTime = quizAttempt.getStartTime().plusSeconds(quizDurationInSeconds);

            Duration timeElapsed = Duration.between(LocalDateTime.now(), quizEndTime);

            if (timeElapsed.isNegative()) {
                quizAttempt.setRemainingTimeInSeconds(0L);
            } else {
                long remainingSeconds = timeElapsed.getSeconds();
                quizAttempt.setRemainingTimeInSeconds(remainingSeconds);
            }
        } else {
            quizAttempt.setRemainingTimeInSeconds(null);
        }

        quizAttemptRepository.save(quizAttempt);
    }

    /**
     * Deletes the auto-saved progress of a user in a quiz
     * (when the user finished their quiz, so we don't keep necessary information in out database).
     *
     * @param userId The ID of the user.
     * @param quizId The ID of the quiz.
     */
    @Override
    @Transactional
    public void deleteAutoSavedProgress(Long userId, Long quizId) {
        userQuizProgressRepository.deleteByUserIdAndQuizId(userId, quizId);
    }

    /**
     * Retrieves the highest scores of a user in all quizzes.
     *
     * @param userId The ID of the user.
     * @return The list of DTOs representing the highest scores in quizzes.
     */
    @Override
    public List<QuizAttemptDTO> getAllUserHighestScoresInQuizzes(Long userId) {
        List<QuizAttempt> quizAttempts = quizAttemptRepository.findByUserId(userId);

        List<QuizAttemptDTO> quizAttemptDTOs = quizAttempts.stream()
                .map(quizAttempt -> modelMapper.map(quizAttempt, QuizAttemptDTO.class)).toList();

        Map<Long, List<QuizAttemptDTO>> quizAttemptsByQuizId = quizAttemptDTOs.stream()
                .collect(Collectors.groupingBy(quizAttemptDTO -> quizAttemptDTO.getQuiz().getId()));

        List<QuizAttemptDTO> highestScores = new ArrayList<>();
        for (Map.Entry<Long, List<QuizAttemptDTO>> entry : quizAttemptsByQuizId.entrySet()) {
            List<QuizAttemptDTO> attemptsForQuiz = entry.getValue();

            QuizAttemptDTO highestScoreAttempt = attemptsForQuiz.stream()
                    .max(Comparator.comparing(QuizAttemptDTO::getTotalMarks))
                    .orElse(null);

            if (highestScoreAttempt != null) {
                highestScores.add(highestScoreAttempt);
            }
        }

        return highestScores;
    }

    /**
     * Calculates the time left in minutes for a quiz attempt.
     *
     * @param quizAttemptId         The ID of the quiz attempt.
     * @param quizDurationInMinutes The duration of the quiz in minutes.
     * @return The time left in minutes for the quiz attempt.
     */
    public long calculateTimeLeftForQuizAttempt(Long quizAttemptId, int quizDurationInMinutes) {
        QuizAttempt quizAttempt = quizAttemptRepository.findById(quizAttemptId)
                .orElseThrow(() -> new QuizAttemptNotFoundException(messageSource));
        LocalDateTime startTime = quizAttempt.getStartTime();
        LocalDateTime currentTime = LocalDateTime.now();
        long elapsedTimeInMinutes = Duration.between(startTime, currentTime).toMinutes();
        long timeLeft = quizDurationInMinutes - elapsedTimeInMinutes;
        return Math.max(timeLeft, 0);
    }

    /**
     * Calculates the quiz success percentage for the current user.
     *
     * @param publicUserDTO The DTO representing the public user.
     * @return The list of DTOs representing user course details with quiz success percentage.
     */
    @Override
    public List<UserCourseDTO> calculateQuizSuccessPercentageForCurrentUser(PublicUserDTO publicUserDTO) {
        List<QuizAttempt> quizAttempts = quizAttemptRepository.findByUserId(publicUserDTO.getId());

        Map<Long, List<QuizAttempt>> quizAttemptsByQuizId = quizAttempts.stream()
                .collect(Collectors.groupingBy(quizAttempt -> quizAttempt.getQuiz().getId()));

        Map<Long, BigDecimal> highestScoresByQuizId = new HashMap<>();

        for (List<QuizAttempt> attemptsForQuiz : quizAttemptsByQuizId.values()) {
            BigDecimal highestScoreForQuiz = attemptsForQuiz.stream()
                    .map(QuizAttempt::getTotalMarks)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
            highestScoresByQuizId.put(attemptsForQuiz.get(0).getQuiz().getId(), highestScoreForQuiz);
        }

        Map<Long, List<Quiz>> quizzesByCourseId = highestScoresByQuizId.keySet().stream()
                .map(quizId -> quizRepository.findById(quizId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(quiz -> quiz.getSubsection().getCourse().getId()));

        List<UserCourseDTO> userCourseDTOs = new ArrayList<>();
        for (Map.Entry<Long, List<Quiz>> entry : quizzesByCourseId.entrySet()) {
            Long courseId = entry.getKey();
            Course course = courseRepository.findByIdAndDeletedFalse(courseId).orElseThrow(() -> new CourseNotFoundException(messageSource));
            CourseResponseDTO courseResponseDTO = modelMapper.map(course, CourseResponseDTO.class);
            List<Quiz> quizzesInCourse = entry.getValue();

            BigDecimal totalHighestScore = BigDecimal.ZERO;
            BigDecimal totalPossibleScore = BigDecimal.ZERO;

            for (Quiz quiz : quizzesInCourse) {
                BigDecimal highestScore = highestScoresByQuizId.getOrDefault(quiz.getId(), BigDecimal.ZERO);
                totalHighestScore = totalHighestScore.add(highestScore);
                totalPossibleScore = totalPossibleScore.add(quiz.getTotalMarks());
            }

            BigDecimal quizSuccessPercentage = BigDecimal.ZERO;
            if (totalPossibleScore.compareTo(BigDecimal.ZERO) > 0) {
                quizSuccessPercentage = totalHighestScore.divide(totalPossibleScore, 2, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }

            UserCourseResponseDTO userCourseDTO = new UserCourseResponseDTO(publicUserDTO.getId(), courseResponseDTO, quizSuccessPercentage);
            userCourseDTOs.add(userCourseDTO);
        }

        return userCourseDTOs;
    }

    /**
     * Retrieves all user progress for a quiz.
     *
     * @param quizId The ID of the quiz.
     * @return The list of DTOs representing user quiz progress.
     */
    private List<UserQuizProgressDTO> getAllUserProgressForQuiz(Long quizId) {
        List<UserQuizProgress> userQuizProgressList = userQuizProgressRepository.findByQuizId(quizId);
        return userQuizProgressList.stream()
                .map(progress -> modelMapper.map(progress, UserQuizProgressDTO.class))
                .collect(Collectors.toList());
    }

    /**
     * Checks if the user's answer is correct.
     *
     * @param userAnswer     The DTO representing the user's answer.
     * @param correctAnswers The list of DTOs representing correct answers.
     * @return True if the user's answer is correct, false otherwise.
     */
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

    /**
     * Checks if the user is the creator of the quiz.
     *
     * @param userId The ID of the user.
     * @param quiz   The quiz entity.
     * @return True if the user is the creator of the quiz, false otherwise.
     */
    private boolean isTheUserQuizCreator(Long userId, Quiz quiz) {
        CourseSubsection courseSubsection = courseSubsectionRepository.findByIdAndDeletedFalse(quiz.getSubsection().getId()).orElseThrow(() -> new CourseSubsectionNotFoundException(messageSource));
        Course course = courseRepository.findByIdAndDeletedFalse(courseSubsection.getCourse().getId()).orElseThrow(() -> new CourseNotFoundException(messageSource));
        return Objects.equals(course.getUser().getId(), userId);
    }
}
