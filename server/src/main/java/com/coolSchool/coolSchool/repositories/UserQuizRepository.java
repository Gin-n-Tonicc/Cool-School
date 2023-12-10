package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.Quiz;
import com.coolSchool.coolSchool.models.entity.User;
import com.coolSchool.coolSchool.models.entity.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuizRepository extends JpaRepository<UserQuiz, Long> {
    List<UserQuiz> findByDeletedFalse();

    Optional<UserQuiz> findByIdAndDeletedFalse(Long id);

    List<UserQuiz> findByUser_Id(Integer id);

    @Query("SELECT uq FROM UserQuiz uq WHERE uq.user = ?1 AND uq.quiz = ?2")
    List<UserQuiz> findByUserAndQuiz(User user, Quiz quiz);
}
