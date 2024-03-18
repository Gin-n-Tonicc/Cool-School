package com.coolSchool.coolSchool.services.impl;

import com.coolSchool.coolSchool.exceptions.courseSubsection.CourseSubsectionNotFoundException;
import com.coolSchool.coolSchool.exceptions.files.FileNotFoundException;
import com.coolSchool.coolSchool.exceptions.resource.ResourceNotFoundException;
import com.coolSchool.coolSchool.models.dto.request.ResourceRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.ResourceResponseDTO;
import com.coolSchool.coolSchool.models.entity.Resource;
import com.coolSchool.coolSchool.repositories.CourseSubsectionRepository;
import com.coolSchool.coolSchool.repositories.FileRepository;
import com.coolSchool.coolSchool.repositories.ResourceRepository;
import com.coolSchool.coolSchool.services.ResourceService;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;
    private final ModelMapper modelMapper;
    private final CourseSubsectionRepository courseSubsectionRepository;
    private final FileRepository fileRepository;
    private final MessageSource messageSource;

    public ResourceServiceImpl(ResourceRepository resourceRepository, ModelMapper modelMapper, CourseSubsectionRepository courseSubsectionRepository, FileRepository fileRepository, MessageSource messageSource) {
        this.resourceRepository = resourceRepository;
        this.modelMapper = modelMapper;
        this.courseSubsectionRepository = courseSubsectionRepository;
        this.fileRepository = fileRepository;
        this.messageSource = messageSource;
    }


    @Override
    public List<ResourceResponseDTO> getAllResources() {
        List<Resource> resources = resourceRepository.findByDeletedFalse();
        return resources.stream().map(resource -> modelMapper.map(resource, ResourceResponseDTO.class)).toList();
    }

    @Override
    public List<ResourceResponseDTO> getBySubsection(Long id) {
        List<Resource> resources = resourceRepository.findAllBySubsectionIdAndDeletedFalse(id);
        return resources.stream().map(resource -> modelMapper.map(resource, ResourceResponseDTO.class)).toList();
    }

    @Override
    public ResourceResponseDTO getResourceById(Long id) {
        Optional<Resource> resource = resourceRepository.findByIdAndDeletedFalse(id);
        if (resource.isPresent()) {
            return modelMapper.map(resource.get(), ResourceResponseDTO.class);
        }
        throw new ResourceNotFoundException(messageSource);
    }

    @Override
    public ResourceResponseDTO createResource(ResourceRequestDTO resourceDTO) {
        resourceDTO.setId(null);
        fileRepository.findByIdAndDeletedFalse(resourceDTO.getFileId()).orElseThrow(() -> new FileNotFoundException(messageSource));
        courseSubsectionRepository.findByIdAndDeletedFalse(resourceDTO.getSubsectionId()).orElseThrow(() -> new CourseSubsectionNotFoundException(messageSource));
        Resource resourceEntity = resourceRepository.save(modelMapper.map(resourceDTO, Resource.class));
        return modelMapper.map(resourceEntity, ResourceResponseDTO.class);
    }

    @Override
    public ResourceResponseDTO updateResource(Long id, ResourceRequestDTO resourceDTO) {
        Optional<Resource> existingResourceOptional = resourceRepository.findByIdAndDeletedFalse(id);

        if (existingResourceOptional.isEmpty()) {
            throw new ResourceNotFoundException(messageSource);
        }
        // Check if this file and course actually exist
        fileRepository.findByIdAndDeletedFalse(resourceDTO.getFileId()).orElseThrow(() -> new FileNotFoundException(messageSource));
        courseSubsectionRepository.findByIdAndDeletedFalse(resourceDTO.getSubsectionId()).orElseThrow(() -> new CourseSubsectionNotFoundException(messageSource));

        Resource existingResource = existingResourceOptional.get();
        modelMapper.map(resourceDTO, existingResource);

        existingResource.setId(id);
        Resource updatedResource = resourceRepository.save(existingResource);
        return modelMapper.map(updatedResource, ResourceResponseDTO.class);
    }

    @Override
    public void deleteResource(Long id) {
        Optional<Resource> resource = resourceRepository.findByIdAndDeletedFalse(id);
        if (resource.isPresent()) {
            // Soft delete
            resource.get().setDeleted(true);
            resourceRepository.save(resource.get());
        } else {
            throw new ResourceNotFoundException(messageSource);
        }
    }
}
