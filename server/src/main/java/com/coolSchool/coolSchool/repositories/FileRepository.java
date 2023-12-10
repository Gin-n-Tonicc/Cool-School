package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByIdAndDeletedFalse(Long id);
}
