package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.UserQuiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserQuizRepository extends JpaRepository<UserQuiz, Long> {
    List<UserQuiz> findByDeletedFalse();

    Optional<UserQuiz> findByIdAndDeletedFalse(Long id);
}
