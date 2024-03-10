package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByDeletedFalse();

    Optional<Answer> findByIdAndDeletedFalse(Long id);

    @Query("SELECT a FROM Answer a WHERE a.questionId.id = :questionId AND a.isCorrect = true")
    List<Answer> findCorrectAnswersByQuestionId(Long questionId);

    @Query("SELECT a FROM Answer a WHERE a.questionId.id = :questionId")
    List<Answer> findAnswersByQuestionId(Long questionId);
}