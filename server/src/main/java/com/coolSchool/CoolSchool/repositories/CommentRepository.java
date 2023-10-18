package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByDeletedFalse();

    Optional<Comment> findByIdAndDeletedFalse(Long id);

    @Query("SELECT b FROM Comment b WHERE b.deleted = false ORDER BY b.created_at DESC")
    List<Comment> findAllByNewestFirst();

    @Query("SELECT b FROM Comment b WHERE b.deleted = false ORDER BY SIZE(b.liked_users) DESC")
    List<Comment> findAllByMostLiked();
}
