package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByDeletedFalseOrderByCreatedDateDesc();

    Optional<Course> findByIdAndDeletedFalse(Long id);

}
