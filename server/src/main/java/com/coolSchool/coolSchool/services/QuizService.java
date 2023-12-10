package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.common.QuizDTO;
import com.coolSchool.coolSchool.models.dto.common.QuizDataDTO;

import java.util.List;

public interface QuizService {
    List<QuizDTO> getAllQuizzes();

    QuizDTO getQuizById(Long id);

    List<QuizDTO> getQuizzesBySubsectionId(Long subsectionId);

    QuizDTO createQuiz(QuizDataDTO quizData);

    QuizDTO updateQuiz(Long id, QuizDTO quizDTO);

    void deleteQuiz(Long id);
}
