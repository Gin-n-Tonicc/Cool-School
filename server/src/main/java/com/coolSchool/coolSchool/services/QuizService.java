package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.common.*;

import java.util.List;

public interface QuizService {
    List<QuizDTO> getAllQuizzes();

    QuizQuestionsAnswersDTO getQuizById(Long id, Long userId);

    List<QuizDTO> getQuizzesBySubsectionId(Long subsectionId);

    QuizDTO createQuiz(QuizDataDTO quizData);

    QuizDTO updateQuiz(Long id, QuizDataDTO quizDTO);

    void deleteQuiz(Long id);
    QuizResultDTO takeQuiz(Long quizId, List<UserAnswerDTO> userAnswers, Long userId);
    void autoSaveUserProgress(Long quizId, Long questionId, Long answerId, Long userId);
    void deleteAutoSavedProgress(Long userId, Long quizId);
}
