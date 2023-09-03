package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByDeletedFalse();

    Optional<Quiz> findByIdAndDeletedFalse(Long id);

    List<Quiz> findBySubsectionIdAndDeletedFalse(Long id);
}
