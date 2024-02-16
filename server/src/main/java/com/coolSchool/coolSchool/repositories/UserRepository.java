package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndDeletedFalse(Long id);

    List<User> findByEnabledFalseAndCreatedAtBefore(LocalDateTime thresholdDateTime);
}
