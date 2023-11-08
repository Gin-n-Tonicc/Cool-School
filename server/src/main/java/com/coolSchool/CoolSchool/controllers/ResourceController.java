package com.coolSchool.CoolSchool.controllers;

import com.coolSchool.CoolSchool.models.dto.request.ResourceRequestDTO;
import com.coolSchool.CoolSchool.models.dto.response.ResourceResponseDTO;
import com.coolSchool.CoolSchool.services.ResourceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    public ResponseEntity<ResourceResponseDTO> getResourceById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(resourceService.getResourceById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ResourceResponseDTO> createResource(@Valid @RequestBody ResourceRequestDTO resourceDTO) {
        ResourceResponseDTO cratedResource = resourceService.createResource(resourceDTO);
        return new ResponseEntity<>(cratedResource, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponseDTO> updateResource(@PathVariable("id") Long id, @Valid @RequestBody ResourceRequestDTO resourceDTO) {
        return ResponseEntity.ok(resourceService.updateResource(id, resourceDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteResourceById(@PathVariable("id") Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.ok("Resource with id: " + id + " has been deleted successfully!");
    }
}
