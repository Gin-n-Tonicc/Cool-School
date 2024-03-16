package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.coolSchool.config.schedulers.QuizAttemptTimer;
import com.coolSchool.coolSchool.exceptions.quizzes.*;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.*;
import com.coolSchool.coolSchool.models.entity.*;
import com.coolSchool.coolSchool.repositories.*;
import com.coolSchool.coolSchool.services.AnswerService;
import com.coolSchool.coolSchool.services.QuestionService;
import com.coolSchool.coolSchool.services.QuizService;
import com.coolSchool.coolSchool.services.UserService;
import com.coolSchool.coolSchool.services.impl.QuizServiceImpl;
import jakarta.transaction.Transactional;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceImplTest {
    ModelMapper modelMapper;
    @InjectMocks
    private QuizServiceImpl quizService;
    @Mock
    private QuizService quizServiceInterface;
    @Mock
    private QuizRepository quizRepository;
    @Mock
    private QuestionService questionService;
    @Mock
    private AnswerService answerService;
    @Mock
    private UserService userService;
    @Mock
    private UserAnswerRepository userAnswerRepository;
    @Mock
    private QuizAttemptRepository quizAttemptRepository;
    @Mock
    private CourseSubsectionRepository courseSubsectionRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private UserQuizProgressRepository userQuizProgressRepository;
    @Mock
    private MessageSource messageSource;
    @InjectMocks
    private QuizAttemptTimer quizAttemptTimer;
    @Mock
    private UserCourseRepository userCourseRepository;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        MockitoAnnotations.openMocks(this);
        quizService = new QuizServiceImpl(quizRepository, modelMapper, questionService, answerService, userService, userAnswerRepository, quizAttemptRepository, courseSubsectionRepository, courseRepository, userQuizProgressRepository, messageSource, quizAttemptTimer, userCourseRepository);
    }

    @Test
    void testGetQuizInfoById_ExistingQuiz() {
        Course course = new Course();
        course.setId(1L);

        CourseSubsection courseSubsection = new CourseSubsection();
        courseSubsection.setId(1L);
        courseSubsection.setCourse(course);

        Long quizId = 1L;
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setSubsection(courseSubsection);

        when(quizRepository.findByIdAndDeletedFalse(quizId)).thenReturn(Optional.of(quiz));

        QuizDTO result = quizService.getQuizInfoById(quizId);

        assertEquals(quizId, result.getId());
    }

    @Test
    void testGetQuizInfoById_NonExistingQuiz() {
        Long quizId = 1L;
        when(quizRepository.findByIdAndDeletedFalse(quizId)).thenReturn(Optional.empty());

        assertThrows(QuizNotFoundException.class, () -> quizService.getQuizInfoById(quizId));
    }

    @Test
    void testGetQuizById_ExistingQuiz() {
        User user = new User();
        user.setId(1L);

        Course course = new Course();
        course.setId(1L);
        course.setUser(user);

        CourseSubsection courseSubsection = new CourseSubsection();
        courseSubsection.setId(1L);
        courseSubsection.setCourse(course);

        Long quizId = 1L;
        Long userId = 1L;
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setSubsection(courseSubsection);

        when(quizRepository.findByIdAndDeletedFalse(quizId)).thenReturn(Optional.of(quiz));
        when(courseSubsectionRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(courseSubsection));
        when(courseRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(course));
        QuizQuestionsAnswersDTO result = quizService.getQuizById(quizId, userId);

        assertEquals(quizId, result.getQuiz().getId());
    }

    @Test
    void testGetQuizById_NonExistingQuiz() {
        Long quizId = 1L;
        Long userId = 1L;
        when(quizRepository.findByIdAndDeletedFalse(quizId)).thenReturn(Optional.empty());

        assertThrows(QuizNotFoundException.class, () -> quizService.getQuizById(quizId, userId));
    }

    @Test
    void testGetAllQuizzes() {
        Quiz quiz = new Quiz();
        when(quizRepository.findByDeletedFalse()).thenReturn(Collections.singletonList(quiz));

        List<QuizDTO> result = quizService.getAllQuizzes();

        assertEquals(1, result.size());
    }

    @Test
    public void testGetQuizzesBySubsectionId() {
        User user = new User();
        user.setId(1L);

        Course course = new Course();
        course.setId(1L);
        course.setUser(user);

        CourseSubsection courseSubsection = new CourseSubsection();
        courseSubsection.setId(1L);
        courseSubsection.setCourse(course);

        Long subsectionId = 1L;
        Quiz quiz1 = new Quiz();
        quiz1.setId(1L);
        quiz1.setDescription("Quiz 1");
        quiz1.setTotalMarks(BigDecimal.valueOf(10));
        quiz1.setAttemptLimit(10);
        quiz1.setStartTime(LocalDateTime.now());
        quiz1.setEndTime(LocalDateTime.now());
        quiz1.setDeleted(false);
        quiz1.setQuizDurationInMinutes(10);
        quiz1.setTitle("title");
        quiz1.setSubsection(courseSubsection);

        List<Quiz> quizzes = new ArrayList<>();
        quizzes.add(quiz1);

        when(quizRepository.findBySubsectionIdAndDeletedFalse(anyLong())).thenReturn(quizzes);

        List<QuizDTO> result = quizService.getQuizzesBySubsectionId(subsectionId);

        assertEquals(1, result.size());
    }

    @Test
    public void testCreateQuiz() {
        QuizDataDTO quizData = new QuizDataDTO();
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(1L);
        quizDTO.setTitle("Test Quiz");

        List<QuestionAndAnswersDTO> questionAndAnswersList = new ArrayList<>();
        QuestionAndAnswersDTO questionAndAnswers1 = new QuestionAndAnswersDTO();
        questionAndAnswers1.setQuestion(new QuestionDTO());
        questionAndAnswers1.getQuestion().setId(1L);
        questionAndAnswers1.getQuestion().setDescription("Question 1");
        questionAndAnswers1.setAnswers(new ArrayList<>());
        questionAndAnswersList.add(questionAndAnswers1);

        quizData.setQuizDTO(quizDTO);
        quizData.setData(questionAndAnswersList);

        Quiz savedQuiz = new Quiz();
        savedQuiz.setId(1L);
        savedQuiz.setDescription("Test Quiz");

        when(quizRepository.save(any(Quiz.class))).thenReturn(savedQuiz);

        when(questionService.createQuestion(any(QuestionDTO.class))).then(invocation -> {
            QuestionDTO questionDTO = invocation.getArgument(0);
            questionDTO.setId(1L);
            questionDTO.setMarks(BigDecimal.TEN);
            return questionDTO;
        });

        QuizDTO result = quizService.createQuiz(quizData);

        assertEquals(quizDTO.getId(), result.getId());

        verify(quizRepository, times(2)).save(any(Quiz.class));
        verify(questionService, times(1)).createQuestion(any(QuestionDTO.class));
    }

    @Test
    public void testCreateQuiz_MultipleCorrectAnswersException() {
        QuizDataDTO quizData = new QuizDataDTO();
        QuizDTO quizDTO = new QuizDTO();
        quizDTO.setId(1L);
        quizDTO.setTitle("Test Quiz");

        List<QuestionAndAnswersDTO> questionAndAnswersList = new ArrayList<>();
        QuestionAndAnswersDTO questionAndAnswers1 = new QuestionAndAnswersDTO();
        questionAndAnswers1.setQuestion(new QuestionDTO());
        questionAndAnswers1.getQuestion().setId(1L);
        questionAndAnswers1.getQuestion().setDescription("Question 1");
        questionAndAnswers1.setAnswers(new ArrayList<>());
        questionAndAnswersList.add(questionAndAnswers1);

        quizData.setQuizDTO(quizDTO);
        quizData.setData(questionAndAnswersList);

        Quiz savedQuiz = new Quiz();
        savedQuiz.setId(1L);
        savedQuiz.setTitle("Test Quiz");

        when(quizRepository.save(any(Quiz.class))).thenReturn(savedQuiz);

        when(questionService.createQuestion(any(QuestionDTO.class))).then(invocation -> {
            QuestionDTO questionDTO = invocation.getArgument(0);
            questionDTO.setId(1L);
            questionDTO.setMarks(BigDecimal.TEN);
            return questionDTO;
        });

        AnswerDTO correctAnswer1 = new AnswerDTO();
        correctAnswer1.setCorrect(true);
        AnswerDTO correctAnswer2 = new AnswerDTO();
        correctAnswer2.setCorrect(true);
        List<AnswerDTO> answersWithMultipleCorrect = new ArrayList<>();
        answersWithMultipleCorrect.add(correctAnswer1);
        answersWithMultipleCorrect.add(correctAnswer2);
        questionAndAnswers1.setAnswers(answersWithMultipleCorrect);

        assertThrows(MultipleCorrectAnswersException.class, () -> quizService.createQuiz(quizData));
    }

    @Test
    public void testUpdateQuiz() {
        Long quizId = 1L;
        QuizDataDTO updatedQuizData = new QuizDataDTO();
        QuizDTO updatedQuizDTO = new QuizDTO();
        updatedQuizDTO.setId(quizId);
        updatedQuizDTO.setTitle("Updated Quiz Title");
        updatedQuizDTO.setDescription("Updated Quiz Description");
        updatedQuizDTO.setStartTime(LocalDateTime.now());
        updatedQuizDTO.setEndTime(LocalDateTime.now());
        updatedQuizDTO.setSubsectionId(1L);
        updatedQuizDTO.setAttemptLimit(2);

        List<QuestionAndAnswersDTO> updatedQuestionAndAnswersList = new ArrayList<>();
        QuestionAndAnswersDTO questionAndAnswersDTO = new QuestionAndAnswersDTO();
        QuestionDTO updatedQuestionDTO = new QuestionDTO();
        updatedQuestionDTO.setId(1L);
        updatedQuestionDTO.setQuizId(quizId);
        updatedQuestionDTO.setDescription("Updated Question Description");
        updatedQuestionDTO.setMarks(BigDecimal.valueOf(5));
        questionAndAnswersDTO.setQuestion(updatedQuestionDTO);
        questionAndAnswersDTO.setAnswers(new ArrayList<>());
        updatedQuestionAndAnswersList.add(questionAndAnswersDTO);
        updatedQuizData.setQuizDTO(updatedQuizDTO);
        updatedQuizData.setData(updatedQuestionAndAnswersList);

        Quiz existingQuiz = new Quiz();
        existingQuiz.setId(quizId);
        existingQuiz.setTitle("Original Quiz Title");
        existingQuiz.setDescription("Original Quiz Description");
        existingQuiz.setStartTime(LocalDateTime.now());
        existingQuiz.setEndTime(LocalDateTime.now());
        existingQuiz.setSubsection(new CourseSubsection());
        existingQuiz.setAttemptLimit(1);

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(existingQuiz));
        when(courseSubsectionRepository.findByIdAndDeletedFalse(updatedQuizDTO.getSubsectionId())).thenReturn(Optional.of(new CourseSubsection()));
        when(quizRepository.save(any(Quiz.class))).thenReturn(existingQuiz);

        QuizDTO updatedQuiz = quizService.updateQuiz(quizId, updatedQuizData);

        assertEquals(updatedQuizDTO.getTitle(), existingQuiz.getTitle());
        assertEquals(updatedQuizDTO.getDescription(), existingQuiz.getDescription());
        assertEquals(updatedQuizDTO.getStartTime(), existingQuiz.getStartTime());
        assertEquals(updatedQuizDTO.getEndTime(), existingQuiz.getEndTime());
        assertEquals(updatedQuizDTO.getAttemptLimit(), existingQuiz.getAttemptLimit());

        verify(questionService, times(1)).updateQuestion(updatedQuestionDTO.getId(), updatedQuestionDTO);
        verify(answerService, times(0)).updateAnswer(anyLong(), any());
    }

    @Test
    public void testDeleteQuiz() {
        Long quizId = 1L;
        Quiz quiz = new Quiz();
        quiz.setId(quizId);

        List<Question> quizQuestions = new ArrayList<>();
        Question question1 = new Question();
        question1.setId(1L);
        quizQuestions.add(question1);

        when(quizRepository.findByIdAndDeletedFalse(quizId)).thenReturn(Optional.of(quiz));
        when(questionService.getQuestionsByQuizId(quizId)).thenReturn(quizQuestions);
        when(answerService.getAnswersByQuestionId(question1.getId())).thenReturn(new ArrayList<>());

        quizService.deleteQuiz(quizId);

        verify(quizRepository, times(1)).save(quiz);
        verify(questionService, times(1)).deleteQuestion(question1.getId());
        verify(answerService, times(0)).deleteAnswer(anyLong());
    }

    @Test
    public void testTakeQuiz_SuccessfulAttempt() {
        Long quizId = 1L;
        Long userId = 1L;
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setAttemptLimit(2);
        quiz.setStartTime(LocalDateTime.now().minusMinutes(5));
        quiz.setEndTime(LocalDateTime.now().plusMinutes(5));
        quiz.setQuizDurationInMinutes(100);

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
        when(userService.findById(userId)).thenReturn(new User());
        when(quizAttemptRepository.countByUserAndQuiz(any(), any())).thenReturn(0);

        when(quizAttemptRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        QuizAttemptDTO quizAttemptDTO = quizService.takeQuiz(quizId, userId);

        Assertions.assertNotNull(quizAttemptDTO);
        verify(quizAttemptRepository, times(2)).save(any());
    }

    @Test
    public void testTakeQuiz_NoMoreAttemptsException() {
        Long quizId = 1L;
        Long userId = 1L;

        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setAttemptLimit(1);
        quiz.setStartTime(LocalDateTime.now().minusMinutes(5));
        quiz.setEndTime(LocalDateTime.now().plusMinutes(5));
        quiz.setQuizDurationInMinutes(100);

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
        when(userService.findById(userId)).thenReturn(new User());
        when(quizAttemptRepository.countByUserAndQuiz(any(), any())).thenReturn(2);

        assertThrows(NoMoreAttemptsQuizException.class, () -> quizService.takeQuiz(quizId, userId));

        verify(quizAttemptRepository, never()).save(any());
    }

    @Test
    public void testSubmitQuiz_SuccessfulSubmission() {
        Long quizId = 1L;
        Long userId = 1L;
        Long attemptId = 1L;
        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        LocalDateTime startTime = LocalDateTime.now().minusMinutes(10);
        LocalDateTime endTime = LocalDateTime.now().plusMinutes(10);
        quiz.setStartTime(startTime);
        quiz.setEndTime(endTime);
        QuestionDTO questionDTO = new QuestionDTO();
        questionDTO.setId(1L);
        questionDTO.setMarks(BigDecimal.TEN);
        AnswerDTO answerDTO = new AnswerDTO();
        answerDTO.setId(1L);
        answerDTO.setCorrect(true);
        UserAnswerDTO userAnswerDTO = new UserAnswerDTO();
        userAnswerDTO.setQuestionId(1L);
        userAnswerDTO.setSelectedOptionId(1L);
        List<UserAnswerDTO> userAnswers = new ArrayList<>();
        userAnswers.add(userAnswerDTO);
        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setId(attemptId);
        quizAttempt.setQuiz(quiz);
        quizAttempt.setUser(new User());
        quizAttempt.setTotalMarks(BigDecimal.TEN);

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));
        when(userService.findById(userId)).thenReturn(new User());
        when(quizAttemptRepository.findById(attemptId)).thenReturn(Optional.of(quizAttempt));
        when(questionService.getQuestionById(1L)).thenReturn(questionDTO);
        when(answerService.getAnswerById(1L)).thenReturn(answerDTO);
        when(answerService.getCorrectAnswersByQuestionId(1L)).thenReturn(List.of(answerDTO));
        when(quizAttemptRepository.save(any(QuizAttempt.class))).thenReturn(quizAttempt);

        QuizResultDTO quizResultDTO = quizService.submitQuiz(quizId, userAnswers, userId, attemptId);

        Assertions.assertNotNull(quizResultDTO);

        verify(userAnswerRepository, times(1)).save(any());
        verify(quizAttemptRepository, times(1)).save(any());
    }


    @Test
    public void testSubmitQuiz_QuizTimeNotValid() {
        Long quizId = 1L;
        Long userId = 1L;
        Long attemptId = 1L;

        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setAttemptLimit(1);
        quiz.setStartTime(LocalDateTime.now().plusMinutes(1000));
        quiz.setEndTime(LocalDateTime.now().minusMinutes(1000));
        quiz.setQuizDurationInMinutes(1);

        when(quizRepository.findById(quizId)).thenReturn(Optional.of(quiz));

        assertThrows(QuizTimeNotValidException.class, () -> quizService.submitQuiz(quizId, new ArrayList<>(), userId, attemptId));

        verify(userAnswerRepository, never()).save(any());
        verify(quizAttemptRepository, never()).save(any());
    }

    @Test
    void testGetAllUserAttemptsInAQuiz_Success() {
        PublicUserDTO publicUserDTO = new PublicUserDTO();
        publicUserDTO.setId(1L);

        QuizAttempt quizAttempt1 = new QuizAttempt();
        QuizAttempt quizAttempt2 = new QuizAttempt();

        List<QuizAttempt> quizAttempts = new ArrayList<>();
        quizAttempts.add(quizAttempt1);
        quizAttempts.add(quizAttempt2);

        when(quizAttemptRepository.findByQuizIdAndUserId(anyLong(), anyLong())).thenReturn(quizAttempts);

        List<QuizAttemptDTO> quizAttemptDTOs = quizService.getAllUserAttemptsInAQuiz(1L, publicUserDTO);

        assertEquals(2, quizAttemptDTOs.size());
    }

    @Test
    @Transactional
    void testAutoSaveUserProgress_Success() {
        Long quizId = 1L;
        Long questionId = 1L;
        Long answerId = 1L;
        Long userId = 1L;
        Long quizAttemptId = 1L;

        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setId(quizAttemptId);

        Quiz quiz = new Quiz();
        quiz.setId(quizId);
        quiz.setQuizDurationInMinutes(60);

        UserQuizProgressDTO userQuizProgressDTO = new UserQuizProgressDTO();
        userQuizProgressDTO.setUserId(userId);
        userQuizProgressDTO.setQuizId(quizId);
        userQuizProgressDTO.setAnswerId(answerId);
        userQuizProgressDTO.setQuestionId(questionId);

        when(quizAttemptRepository.findById(quizAttemptId)).thenReturn(java.util.Optional.of(quizAttempt));
        when(quizRepository.findById(quizId)).thenReturn(java.util.Optional.of(quiz));

        List<UserQuizProgressDTO> userQuizProgressDTOs = quizService.autoSaveUserProgress(quizId, questionId, answerId, userId, quizAttemptId);

        assertEquals(0, userQuizProgressDTOs.size());
    }

    @Test
    @Transactional
    void testDeleteAutoSavedProgress_Success() {
        Long userId = 1L;
        Long quizId = 1L;

        quizService.deleteAutoSavedProgress(userId, quizId);

        verify(userQuizProgressRepository).deleteByUserIdAndQuizId(userId, quizId);
    }

    @Test
    void testGetAllUserHighestScoresInQuizzes_Success() {
        Long userId = 1L;

        Quiz quiz1 = new Quiz();
        quiz1.setId(1L);

        QuizAttempt attempt1 = new QuizAttempt();
        attempt1.setId(1L);
        attempt1.setQuiz(quiz1);
        attempt1.setTotalMarks(BigDecimal.valueOf(90));

        QuizAttempt attempt2 = new QuizAttempt();
        attempt2.setId(2L);
        attempt2.setQuiz(quiz1);
        attempt2.setTotalMarks(BigDecimal.valueOf(80));

        Quiz quiz2 = new Quiz();
        quiz2.setId(2L);

        QuizAttempt attempt3 = new QuizAttempt();
        attempt3.setId(3L);
        attempt3.setQuiz(quiz2);
        attempt3.setTotalMarks(BigDecimal.valueOf(85));

        List<QuizAttempt> mockQuizAttempts = List.of(attempt1, attempt2, attempt3);

        when(quizAttemptRepository.findByUserId(userId)).thenReturn(mockQuizAttempts);

        List<QuizAttemptDTO> result = quizService.getAllUserHighestScoresInQuizzes(userId);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(1L, result.get(0).getId());
        Assertions.assertEquals(BigDecimal.valueOf(90), result.get(0).getTotalMarks());
        Assertions.assertEquals(3L, result.get(1).getId());
        Assertions.assertEquals(BigDecimal.valueOf(85), result.get(1).getTotalMarks());
    }

    @Test
    void testCalculateTimeLeftForQuizAttempt() {
        long quizAttemptId = 1L;
        LocalDateTime startTime = LocalDateTime.now();
        int quizDurationInMinutes = 60;
        long expectedTimeLeft = 60;

        QuizAttempt quizAttempt = new QuizAttempt();
        quizAttempt.setId(quizAttemptId);
        quizAttempt.setStartTime(startTime);

        when(quizAttemptRepository.findById(quizAttemptId)).thenReturn(java.util.Optional.of(quizAttempt));

        long timeLeft = quizService.calculateTimeLeftForQuizAttempt(quizAttemptId, quizDurationInMinutes);

        assertEquals(expectedTimeLeft, timeLeft);
    }

    @Test
    void testCalculateQuizSuccessPercentageForCurrentUser() {
        long userId = 1L;
        PublicUserDTO publicUserDTO = new PublicUserDTO();
        publicUserDTO.setId(userId);

        List<QuizAttempt> quizAttempts = new ArrayList<>();

        when(quizAttemptRepository.findByUserId(userId)).thenReturn(quizAttempts);

        List<UserCourseDTO> userCourseDTOs = quizService.calculateQuizSuccessPercentageForCurrentUser(publicUserDTO);
        assertEquals(0, userCourseDTOs.size());
    }
    @Test
    void testGetQuizAttemptDetails_NotFound() {
        Long quizAttemptId = 1L;

        when(quizAttemptRepository.findById(quizAttemptId)).thenReturn(Optional.empty());

        assertThrows(QuizAttemptNotFoundException.class, () -> quizService.getQuizAttemptDetails(quizAttemptId));
    }
}
