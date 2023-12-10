package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByDeletedFalse();

    Optional<Category> findByIdAndDeletedFalse(Long id);

}
