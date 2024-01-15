package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.UserQuizProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserQuizProgressRepository extends JpaRepository<UserQuizProgress, Long> {
    void deleteByUserIdAndQuizId(Long userId, Long quizId);
}
