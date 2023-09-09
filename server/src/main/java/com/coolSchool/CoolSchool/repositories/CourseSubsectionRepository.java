package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.CourseSubsection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSubsectionRepository extends JpaRepository<CourseSubsection, Long> {
    List<CourseSubsection> findByDeletedFalse();

    Optional<CourseSubsection> findByIdAndDeletedFalse(Long id);

}
