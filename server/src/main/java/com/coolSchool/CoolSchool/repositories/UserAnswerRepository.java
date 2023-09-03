package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.Quiz;
import com.coolSchool.CoolSchool.models.entity.User;
import com.coolSchool.CoolSchool.models.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findByDeletedFalse();

    Optional<UserAnswer> findByIdAndDeletedFalse(Long id);
    List<UserAnswer> findByUserAndAttemptNumber(User user, Integer attemptNumber);
}
