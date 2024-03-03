package com.coolSchool.CoolSchool.controllerTest;

import com.coolSchool.coolSchool.controllers.ResourceController;
import com.coolSchool.coolSchool.models.dto.request.ResourceRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.ResourceResponseDTO;
import com.coolSchool.coolSchool.services.ResourceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResourceControllerIntegrationTest {

    @Mock
    private ResourceService resourceService;

    @InjectMocks
    private ResourceController resourceController;

    @Test
    void testGetAllResources() {
        List<ResourceResponseDTO> resources = new ArrayList<>();
        resources.add(new ResourceResponseDTO());
        resources.add(new ResourceResponseDTO());

        resources.get(0).setId(1L);
        resources.get(0).setName("Resource 1");
        resources.get(1).setId(2L);
        resources.get(1).setName("Resource 2");

        when(resourceService.getAllResources()).thenReturn(resources);

        ResponseEntity<List<ResourceResponseDTO>> responseEntity = resourceController.getAllResources();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(resources, responseEntity.getBody());
    }

    @Test
    void testGetAllResourcesBySubsection() {
        List<ResourceResponseDTO> resources = new ArrayList<>();
        resources.add(new ResourceResponseDTO());
        resources.add(new ResourceResponseDTO());

        resources.get(0).setId(1L);
        resources.get(0).setName("Resource 1");
        resources.get(1).setId(2L);
        resources.get(1).setName("Resource 2");

        when(resourceService.getBySubsection(1L)).thenReturn(resources);

        ResponseEntity<List<ResourceResponseDTO>> responseEntity = resourceController.getAllResourcesBySubsection(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(resources, responseEntity.getBody());
    }

    @Test
    void testGetResourceById() {
        ResourceResponseDTO resource = new ResourceResponseDTO();

        resource.setId(1L);
        resource.setName("Resource 1");

        when(resourceService.getResourceById(1L)).thenReturn(resource);

        ResponseEntity<ResourceResponseDTO> responseEntity = resourceController.getResourceById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(resource, responseEntity.getBody());
    }

    @Test
    void testCreateResource() {
        ResourceRequestDTO resourceDTO = new ResourceRequestDTO();
        ResourceResponseDTO createdResource = new ResourceResponseDTO();

        createdResource.setId(1L);
        createdResource.setName("Updated Resource");

        when(resourceService.createResource(resourceDTO)).thenReturn(createdResource);

        ResponseEntity<ResourceResponseDTO> responseEntity = resourceController.createResource(resourceDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(createdResource, responseEntity.getBody());
    }

    @Test
    void testUpdateResource() {
        ResourceRequestDTO resourceDTO = new ResourceRequestDTO();
        ResourceResponseDTO updatedResource = new ResourceResponseDTO();

        updatedResource.setId(1L);
        updatedResource.setName("Updated Resource");

        when(resourceService.updateResource(1L, resourceDTO)).thenReturn(updatedResource);

        ResponseEntity<ResourceResponseDTO> responseEntity = resourceController.updateResource(1L, resourceDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedResource, responseEntity.getBody());
    }

    @Test
    void testDeleteResourceById() {
        ResponseEntity<String> responseEntity = resourceController.deleteResourceById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Resource with id: 1 has been deleted successfully!", responseEntity.getBody());
    }
}
