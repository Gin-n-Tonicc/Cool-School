package com.coolSchool.coolSchool.repositories;

import com.coolSchool.coolSchool.models.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByDeletedFalse();

    List<Resource> findAllBySubsectionIdAndDeletedFalse(Long id);

    Optional<Resource> findByIdAndDeletedFalse(Long id);
}
