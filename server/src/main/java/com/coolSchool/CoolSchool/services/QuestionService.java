package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.common.QuestionDTO;

import java.util.List;

public interface QuestionService {
    List<QuestionDTO> getAllQuestions();

    QuestionDTO getQuestionById(Long id);

    QuestionDTO createQuestion(QuestionDTO questionDTO);

    QuestionDTO updateQuestion(Long id, QuestionDTO questionDTO);

    void deleteQuestion(Long id);
}
