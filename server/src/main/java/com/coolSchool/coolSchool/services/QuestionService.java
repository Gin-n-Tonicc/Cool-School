package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.common.QuestionDTO;
import com.coolSchool.coolSchool.models.entity.Question;

import java.util.List;

public interface QuestionService {
    List<QuestionDTO> getAllQuestions();

    QuestionDTO getQuestionById(Long id);

    QuestionDTO createQuestion(QuestionDTO questionDTO);

    QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO);

    void deleteQuestion(Long id);

    List<Question> getQuestionsByQuizId(Long quizId);
}
