package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
}
