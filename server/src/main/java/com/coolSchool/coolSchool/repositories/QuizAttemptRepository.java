package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.Quiz;
import com.coolSchool.coolSchool.models.entity.QuizAttempt;
import com.coolSchool.coolSchool.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    int countByUserAndQuiz(User user, Quiz quiz);
}
