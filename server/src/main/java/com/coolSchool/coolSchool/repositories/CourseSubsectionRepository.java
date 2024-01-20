package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.CourseSubsection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSubsectionRepository extends JpaRepository<CourseSubsection, Long> {
    List<CourseSubsection> findByDeletedFalse();

    List<CourseSubsection> findAllByCourseIdAndDeletedFalse(Long id);

    Optional<CourseSubsection> findByIdAndDeletedFalse(Long id);

}
