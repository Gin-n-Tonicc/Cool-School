package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByDeletedFalse();

    Optional<Answer> findByIdAndDeletedFalse(Long id);
}