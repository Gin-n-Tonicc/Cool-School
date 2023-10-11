package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.AnswerDTO;

import java.util.List;

public interface AnswerService {
    List<AnswerDTO> getAllAnswers();

    AnswerDTO getAnswerById(Long id);

    AnswerDTO createAnswer(AnswerDTO answerDTO);

    AnswerDTO updateAnswer(Long id, AnswerDTO answerDTO);

    void deleteAnswer(Long id);
}
