package com.coolSchool.CoolSchool.services;

import com.coolSchool.CoolSchool.models.dto.request.ResourceRequestDTO;
import com.coolSchool.CoolSchool.models.dto.response.ResourceResponseDTO;

import java.util.List;

public interface ResourceService {
    List<ResourceResponseDTO> getAllResources();

    ResourceResponseDTO getResourceById(Long id);

    ResourceResponseDTO createResource(ResourceRequestDTO resourceDTO);

    ResourceResponseDTO updateResource(Long id, ResourceRequestDTO resourceDTO);

    void deleteResource(Long id);
}
