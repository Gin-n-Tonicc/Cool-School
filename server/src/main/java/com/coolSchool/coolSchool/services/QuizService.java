package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.common.*;

import java.util.List;

public interface QuizService {
    List<QuizDTO> getAllQuizzes();

    QuizDTO getQuizInfoById(Long id);

    QuizQuestionsAnswersDTO getQuizById(Long id, Long userId);

    List<QuizDTO> getQuizzesBySubsectionId(Long subsectionId);

    QuizDTO createQuiz(QuizDataDTO quizData);

    QuizDTO updateQuiz(Long id, QuizDataDTO quizDTO);

    void deleteQuiz(Long id);

    QuizAttemptDTO takeQuiz(Long quizId, Long userId);

    QuizResultDTO submitQuiz(Long quizId, List<UserAnswerDTO> userAnswers, Long userId, Long attemptId);

    List<UserQuizProgressDTO> autoSaveUserProgress(Long quizId, Long questionId, Long answerId, Long userId, Long quizAttemptId);

    void deleteAutoSavedProgress(Long userId, Long quizId);

    QuizAttemptDTO getQuizAttemptDetails(Long quizAttemptId);

    List<QuizAttemptDTO> getAllUserAttemptsInAQuiz(Long quizId, PublicUserDTO publicUserDTO);

    List<QuizAttemptDTO> getAllUserHighestScoresInQuizzes(Long userId);

    List<UserCourseDTO> calculateQuizSuccessPercentageForCurrentUser(PublicUserDTO publicUserDTO);
}
