package com.coolSchool.coolSchool.services;

import com.coolSchool.coolSchool.models.dto.request.ResourceRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.ResourceResponseDTO;

import java.util.List;

public interface ResourceService {
    List<ResourceResponseDTO> getAllResources();

    List<ResourceResponseDTO> getBySubsection(Long id);

    ResourceResponseDTO getResourceById(Long id);

    ResourceResponseDTO createResource(ResourceRequestDTO resourceDTO);

    ResourceResponseDTO updateResource(Long id, ResourceRequestDTO resourceDTO);

    void deleteResource(Long id);
}
