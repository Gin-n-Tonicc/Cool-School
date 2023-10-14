package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findByDeletedFalse();

    Optional<Blog> findByIdAndDeletedFalse(Long id);
}