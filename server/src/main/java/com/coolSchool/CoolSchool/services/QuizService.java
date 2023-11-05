package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.QuizDTO;
import com.coolSchool.CoolSchool.models.dto.QuizDataDTO;
import com.coolSchool.CoolSchool.models.entity.Quiz;

import java.util.List;

public interface QuizService {
    List<QuizDTO> getAllQuizzes();

    QuizDTO getQuizById(Long id);

    List<QuizDTO> getQuizzesBySubsectionId(Long subsectionId);

    QuizDTO createQuiz(QuizDataDTO quizData);

    QuizDTO updateQuiz(Long id, QuizDTO quizDTO);

    void deleteQuiz(Long id);
}
