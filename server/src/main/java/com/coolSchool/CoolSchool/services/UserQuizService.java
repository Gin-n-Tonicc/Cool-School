package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.UserQuizDTO;

import java.util.List;

public interface UserQuizService {
    List<UserQuizDTO> getAllUserQuizzes();

    UserQuizDTO getUserQuizById(Long id);

    UserQuizDTO createUserQuiz(UserQuizDTO userQuizDTO);

    UserQuizDTO updateUserQuiz(Long id, UserQuizDTO userQuizDTO);

    void deleteUserQuiz(Long id);

    List<UserQuizDTO> calculateUserTotalMarks(Long userId, Long quizId);

}
