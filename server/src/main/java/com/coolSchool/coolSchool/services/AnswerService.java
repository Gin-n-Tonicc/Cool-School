package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.common.AnswerDTO;

import java.util.List;

public interface AnswerService {
    List<AnswerDTO> getAllAnswers();

    AnswerDTO getAnswerById(Long id);

    AnswerDTO createAnswer(AnswerDTO answerDTO);

    AnswerDTO updateAnswer(Long id, AnswerDTO answerDTO);

    void deleteAnswer(Long id);

    List<AnswerDTO> getCorrectAnswersByQuestionId(Long questionId);

    List<AnswerDTO> getAnswersByQuestionId(Long questionId);
}
