package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.ResourceDTO;

import java.util.List;

public interface ResourceService {
    List<ResourceDTO> getAllResources();

    ResourceDTO getResourceById(Long id);

    ResourceDTO createResource(ResourceDTO resourceDTO);

    ResourceDTO updateResource(Long id, ResourceDTO resourceDTO);

    void deleteResource(Long id);
}
