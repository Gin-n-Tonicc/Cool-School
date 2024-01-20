package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.Course;
import com.coolSchool.coolSchool.models.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByDeletedFalse();

    Optional<Review> findByIdAndDeletedFalse(Long id);

    @Query("SELECT r FROM Review r WHERE r.course = :course AND r.deleted = false ORDER BY r.id DESC LIMIT 5")
    List<Review> findAllByCourse(@Param("course") Course course);
}
