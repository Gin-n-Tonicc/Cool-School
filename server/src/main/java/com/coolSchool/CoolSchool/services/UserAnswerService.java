package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.UserAnswerDTO;

import java.util.List;

public interface UserAnswerService {
    List<UserAnswerDTO> getAllUserAnswers();

    UserAnswerDTO getUserAnswerById(Long id);

    UserAnswerDTO createUserAnswer(UserAnswerDTO userAnswerDTO);

    UserAnswerDTO updateUserAnswer(Long id, UserAnswerDTO userAnswerDTO);

    void deleteUserAnswer(Long id);
}
