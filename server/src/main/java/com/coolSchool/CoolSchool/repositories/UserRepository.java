package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByIdAndDeletedFalse(Long id);
}
