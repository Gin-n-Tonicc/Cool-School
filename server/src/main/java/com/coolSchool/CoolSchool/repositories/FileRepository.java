package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {
    Optional<File> findByIdAndDeletedFalse(Long id);
}
