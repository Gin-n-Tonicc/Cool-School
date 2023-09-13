package com.coolSchool.CoolSchool.repositories;

import com.coolSchool.CoolSchool.models.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByDeletedFalse();

    Optional<Resource> findByIdAndDeletedFalse(Long id);
}
