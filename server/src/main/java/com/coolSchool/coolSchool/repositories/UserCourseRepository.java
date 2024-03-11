package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    List<UserCourse> findByDeletedFalse();

    List<UserCourse> findByUserId(Long id);

    Optional<UserCourse> findByIdAndDeletedFalse(Long id);

    boolean existsByUserIdAndCourseIdAndDeletedFalse(Long userId, Long courseId);

}
