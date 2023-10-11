package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.QuizDTO;

import java.util.List;

public interface QuizService {
    List<QuizDTO> getAllQuizzes();

    QuizDTO getQuizById(Long id);

    List<QuizDTO> getQuizzesBySubsectionId(Long subsectionId);

    QuizDTO createQuiz(QuizDTO quizDTO);

    QuizDTO updateQuiz(Long id, QuizDTO quizDTO);

    void deleteQuiz(Long id);
}
