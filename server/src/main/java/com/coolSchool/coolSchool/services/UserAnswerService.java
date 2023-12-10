package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.common.UserAnswerDTO;

import java.util.List;

public interface UserAnswerService {
    List<UserAnswerDTO> getAllUserAnswers();

    UserAnswerDTO getUserAnswerById(Long id);

    UserAnswerDTO createUserAnswer(UserAnswerDTO userAnswerDTO);

    UserAnswerDTO updateUserAnswer(Long id, UserAnswerDTO userAnswerDTO);

    void deleteUserAnswer(Long id);

    Integer calculateTheNextAttemptNumber(Long userId, Long answerId);
}
