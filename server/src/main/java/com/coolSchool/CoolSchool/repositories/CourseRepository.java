package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByDeletedFalse();

    Optional<Course> findByIdAndDeletedFalse(Long id);

}