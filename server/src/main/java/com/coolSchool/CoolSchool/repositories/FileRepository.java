package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
