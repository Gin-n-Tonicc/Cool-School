package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query("SELECT b FROM Blog b WHERE b.deleted = false AND b.isEnabled = true")
    List<Blog> findByDeletedFalseAndIsEnabledTrue();

    @Query("SELECT b FROM Blog b WHERE b.id = :id AND b.deleted = false AND b.isEnabled = true")
    Optional<Blog> findByIdAndDeletedFalseIsEnabledTrue(Long id);

    @Query("SELECT b FROM Blog b WHERE b.deleted = false AND b.isEnabled = true ORDER BY b.created_at DESC")
    List<Blog> findAllByNewestFirst();

    @Query("SELECT b FROM Blog b WHERE b.deleted = false AND b.isEnabled = true ORDER BY SIZE(b.liked_users) DESC")
    List<Blog> findAllByMostLiked();

    @Query("SELECT b FROM Blog b WHERE LOWER(b.title) LIKE %:keyword% AND b.deleted = false AND b.isEnabled = true")
    List<Blog> searchByTitleContainingIgnoreCase(@Param("keyword") String keyword);

    @Query("SELECT b FROM Blog b JOIN b.categoryId c " +
            "WHERE c.name LIKE %:categoryName% " + "AND b.deleted = false AND b.isEnabled = true")
    List<Blog> findByCategoryIdName(String categoryName);

    @Query("SELECT b FROM Blog b JOIN b.categoryId c " +
            "WHERE (LOWER(b.title) LIKE %:keyword% OR c.name LIKE %:keyword%) " +
            "AND b.deleted = false AND b.isEnabled = true")
    List<Blog> searchByTitleAndCategoryName(@Param("keyword") String keyword);
}