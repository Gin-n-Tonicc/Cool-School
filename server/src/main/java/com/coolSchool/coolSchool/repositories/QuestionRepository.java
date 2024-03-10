package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByDeletedFalse();

    Optional<Question> findByIdAndDeletedFalse(Long id);

    List<Question> findByQuizId(Long quizId);
}
