package com.coolSchool.CoolSchool.repositories;

import java.util.Optional;

import com.coolSchool.CoolSchool.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);
}
