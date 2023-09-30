package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByDeletedFalse();

    Optional<Message> findByIdAndDeletedFalse(Long id);
}
