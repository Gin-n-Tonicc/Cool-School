package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.interfaces.RateLimited;
import com.coolSchool.coolSchool.models.dto.request.ResourceRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.ResourceResponseDTO;
import com.coolSchool.coolSchool.services.ResourceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A controller class for handling resource-related operations.
 * (Resource are the materials given by the teacher in course)
 */
@RestController
@RequestMapping("/api/v1/resources")
public class ResourceController {
    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<ResourceResponseDTO>> getAllResources() {
        return ResponseEntity.ok(resourceService.getAllResources());
    }

    @GetMapping("/subsection/{id}")
    public ResponseEntity<List<ResourceResponseDTO>> getAllResourcesBySubsection(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(resourceService.getBySubsection(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponseDTO> getResourceById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(resourceService.getResourceById(id));
    }

    @RateLimited
    @PostMapping("/create")
    public ResponseEntity<ResourceResponseDTO> createResource(@Valid @RequestBody ResourceRequestDTO resourceDTO) {
        ResourceResponseDTO cratedResource = resourceService.createResource(resourceDTO);
        return new ResponseEntity<>(cratedResource, HttpStatus.CREATED);
    }

    @RateLimited
    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponseDTO> updateResource(@PathVariable("id") Long id, @Valid @RequestBody ResourceRequestDTO resourceDTO) {
        return ResponseEntity.ok(resourceService.updateResource(id, resourceDTO));
    }

    @RateLimited
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResourceById(@PathVariable("id") Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.ok("Resource with id: " + id + " has been deleted successfully!");
    }
}
