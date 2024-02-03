package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.UserQuizProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserQuizProgressRepository extends JpaRepository<UserQuizProgress, Long> {
    void deleteByUserIdAndQuizId(Long userId, Long quizId);
    List<UserQuizProgress> findByUserIdAndQuizId(Long userId, Long quizId);
    @Query("SELECT uqp FROM UserQuizProgress uqp WHERE uqp.quizId = :quizId")
    List<UserQuizProgress> findByQuizId(@Param("quizId") Long quizId);
}
