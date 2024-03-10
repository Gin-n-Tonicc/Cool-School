package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.Quiz;
import com.coolSchool.coolSchool.models.entity.QuizAttempt;
import com.coolSchool.coolSchool.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    int countByUserAndQuiz(User user, Quiz quiz);

    List<QuizAttempt> findByCompletedFalse();

    List<QuizAttempt> findByIdAndCompletedFalse(Long id);

    List<QuizAttempt> findByQuizIdAndUserId(Long quizId, Long userId);

    List<QuizAttempt> findByUserId(Long userId);
}
