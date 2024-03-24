package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.enums.Language;
import com.coolSchool.coolSchool.models.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    @Query("SELECT b FROM Blog b WHERE b.deleted = false AND b.isEnabled = true AND b.language = :language")
    List<Blog> findByLanguageAndDeletedFalseAndIsEnabledTrue(@Param("language") Language language);
    // Method to retrieve blog by ID if it's not deleted and is enabled
    @Query("SELECT b FROM Blog b WHERE b.id = :id AND b.deleted = false AND b.isEnabled = true AND b.language = :language")
    Optional<Blog> findByIdAndDeletedFalseIsEnabledTrue(@Param("id") Long id, @Param("language") Language language);

    // Method to retrieve all blogs sorted by creation date, newest first, for a specific language
    @Query("SELECT b FROM Blog b WHERE b.deleted = false AND b.isEnabled = true AND b.language = :language ORDER BY b.created_at DESC")
    List<Blog> findAllByNewestFirst(@Param("language") Language language);

    // Method to retrieve all blogs sorted by the number of likes, limited to 3, for a specific language
    @Query("SELECT b FROM Blog b WHERE b.deleted = false AND b.isEnabled = true AND b.language = :language ORDER BY SIZE(b.liked_users) DESC")
    List<Blog> findAllByMostLiked(@Param("language") Language language);

    // Method to search blogs by title containing a keyword, for a specific language
    @Query("SELECT b FROM Blog b WHERE LOWER(b.title) LIKE %:keyword% AND b.deleted = false AND b.isEnabled = true AND b.language = :language")
    List<Blog> searchByTitleContainingIgnoreCase(@Param("keyword") String keyword, @Param("language") Language language);

    // Method to retrieve blogs by category name, for a specific language
    @Query("SELECT b FROM Blog b JOIN b.categoryId c " +
            "WHERE c.name LIKE %:categoryName% AND b.deleted = false AND b.isEnabled = true AND b.language = :language")
    List<Blog> findByCategoryIdName(@Param("categoryName") String categoryName, @Param("language") Language language);

    // Method to search blogs by keyword in title and category name, for a specific language
    @Query("SELECT b FROM Blog b " +
            "JOIN b.categoryId c " +
            "WHERE lower(b.title) like lower(concat('%', :titleKeyword, '%')) " +
            "AND lower(c.name) like lower(concat('%', :categoryKeyword, '%')) " +
            "AND b.deleted = false AND b.isEnabled = true AND b.language = :language")
    List<Blog> searchBlogsByKeywordInTitleAndCategory(@Param("titleKeyword") String titleKeyword, @Param("categoryKeyword") String categoryKeyword, @Param("language") Language language);
    List<Blog> findByLanguage(Language language);

    @Query("SELECT b FROM Blog b " +
            "JOIN b.categoryId c " +
            "WHERE lower(b.title) like lower(concat('%', :titleKeyword, '%')) " +
            "OR lower(c.name) like lower(concat('%', :categoryKeyword, '%')) " +
            "AND b.deleted = false " +
            "AND b.isEnabled = true")
    List<Blog> searchBlogsByKeywordInTitleOrCategory(String titleKeyword, String categoryKeyword);
}