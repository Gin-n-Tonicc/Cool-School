package com.coolSchool.CoolSchool.serviceTest;

import com.coolSchool.CoolSchool.exceptions.resource.ResourceNotFoundException;
import com.coolSchool.CoolSchool.exceptions.resource.ValidationResourceException;
import com.coolSchool.CoolSchool.models.dto.request.ResourceRequestDTO;
import com.coolSchool.CoolSchool.models.dto.response.ResourceResponseDTO;
import com.coolSchool.CoolSchool.models.entity.CourseSubsection;
import com.coolSchool.CoolSchool.models.entity.File;
import com.coolSchool.CoolSchool.models.entity.Resource;
import com.coolSchool.CoolSchool.repositories.CourseSubsectionRepository;
import com.coolSchool.CoolSchool.repositories.FileRepository;
import com.coolSchool.CoolSchool.repositories.ResourceRepository;
import com.coolSchool.CoolSchool.services.impl.ResourceServiceImpl;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceServiceImplTest {

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ResourceServiceImpl resourceService;

    private ModelMapper modelMapper;
    private Validator validator;
    @Mock
    private CourseSubsectionRepository courseSubsectionRepository;
    @Mock
    private FileRepository fileRepository;

    @BeforeEach
    void setUp() {
        modelMapper = new ModelMapper();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
        resourceService = new ResourceServiceImpl(resourceRepository, modelMapper, courseSubsectionRepository, fileRepository, validator);
    }

    @Test
    public void testDeleteResource_ResourcePresent() {
        Long resourceId = 1L;

        Resource resource = new Resource();
        resource.setDeleted(false);

        Optional<Resource> resourceOptional = Optional.of(resource);

        when(resourceRepository.findByIdAndDeletedFalse(resourceId)).thenReturn(resourceOptional);
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);

        assertDoesNotThrow(() -> resourceService.deleteResource(resourceId));
        assertTrue(resource.isDeleted());
        verify(resourceRepository, times(1)).save(resource);
    }

    @Test
    void testGetAllResourcezes() {
        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(new Resource());
        Mockito.when(resourceRepository.findByDeletedFalse()).thenReturn(resourceList);
        List<ResourceResponseDTO> result = resourceService.getAllResources();
        assertNotNull(result);
        assertEquals(resourceList.size(), result.size());
    }

    @Test
    void testGetResourceById() {
        Long resourceId = 1L;
        Resource resource = new Resource();
        Optional<Resource> resourceOptional = Optional.of(resource);
        when(resourceRepository.findByIdAndDeletedFalse(resourceId)).thenReturn(resourceOptional);
        ResourceResponseDTO result = resourceService.getResourceById(resourceId);
        assertNotNull(result);
    }

    @Test
    void testGetResourceByIdNotFound() {
        Long resourceId = 1L;
        Optional<Resource> resourceOptional = Optional.empty();
        when(resourceRepository.findByIdAndDeletedFalse(resourceId)).thenReturn(resourceOptional);
        assertThrows(ResourceNotFoundException.class, () -> resourceService.getResourceById(resourceId));
    }

    @Test
    void testCreateResource() {
        ResourceResponseDTO resourceDTO = new ResourceResponseDTO();
        Resource resource = modelMapper.map(resourceDTO, Resource.class);
        when(resourceRepository.save(any(Resource.class))).thenReturn(resource);
        when(fileRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new File()));
        when(courseSubsectionRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new CourseSubsection()));
        ResourceResponseDTO result = resourceService.createResource(modelMapper.map(resourceDTO, ResourceRequestDTO.class));
        assertNotNull(result);
    }

    @Test
    void testUpdateResource() {
        Long resourceId = 1L;
        ResourceResponseDTO updatedResourceDTO = new ResourceResponseDTO();
        Resource existingResource = new Resource();
        Optional<Resource> existingResourceOptional = Optional.of(existingResource);
        when(resourceRepository.findByIdAndDeletedFalse(resourceId)).thenReturn(existingResourceOptional);
        when(resourceRepository.save(any(Resource.class))).thenReturn(existingResource);
        when(fileRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new File()));
        when(courseSubsectionRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new CourseSubsection()));
        ResourceResponseDTO result = resourceService.updateResource(resourceId, modelMapper.map(updatedResourceDTO, ResourceRequestDTO.class));
        assertNotNull(result);
    }

    @Test
    void testUpdateResourceNotFound() {
        Long nonExistentResourceId = 99L;
        ResourceRequestDTO updatedResourceDTO = new ResourceRequestDTO();
        when(resourceRepository.findByIdAndDeletedFalse(nonExistentResourceId)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> resourceService.updateResource(nonExistentResourceId, updatedResourceDTO));
    }

    @Test
    void testDeleteResourceNotFound() {
        Long nonExistentResourceId = 99L;

        when(resourceRepository.findByIdAndDeletedFalse(nonExistentResourceId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resourceService.deleteResource(nonExistentResourceId));
    }

    @Test
    void testCreateResource_ValidationException() {
        ResourceRequestDTO resourceDTO = new ResourceRequestDTO();
        resourceDTO.setFileId(null);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);

        when(resourceRepository.save(any(Resource.class))).thenThrow(constraintViolationException);
        when(fileRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new File()));
        when(courseSubsectionRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new CourseSubsection()));
        assertThrows(ValidationResourceException.class, () -> resourceService.createResource(resourceDTO));
    }

    @Test
    void testUpdateResource_ValidationException() {
        Long resourceId = 1L;
        ResourceRequestDTO resourceDTO = new ResourceRequestDTO();
        resourceDTO.setName(null);
        Resource existingResource = new Resource();
        Optional<Resource> existingResourceOptional = Optional.of(existingResource);

        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Set<ConstraintViolation<?>> violations = Collections.singleton(violation);

        ConstraintViolationException constraintViolationException = new ConstraintViolationException("Validation error", violations);
        when(fileRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new File()));
        when(courseSubsectionRepository.findByIdAndDeletedFalse(any())).thenReturn(Optional.of(new CourseSubsection()));
        when(resourceRepository.findByIdAndDeletedFalse(resourceId)).thenReturn(existingResourceOptional);
        when(resourceRepository.save(any(Resource.class))).thenThrow(constraintViolationException);

        assertThrows(ConstraintViolationException.class, () -> resourceService.updateResource(resourceId, resourceDTO));
    }
}

