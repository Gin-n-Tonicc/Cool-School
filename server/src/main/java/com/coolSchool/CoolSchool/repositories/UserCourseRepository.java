package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.UserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserCourseRepository extends JpaRepository<UserCourse, Long> {
    List<UserCourse> findByDeletedFalse();

    Optional<UserCourse> findByIdAndDeletedFalse(Long id);

    boolean existsByUserIdAndCourseIdAndDeletedFalse(Long userId, Long courseId);

}
