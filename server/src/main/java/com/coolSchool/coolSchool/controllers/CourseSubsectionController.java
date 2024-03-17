package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.interfaces.RateLimited;
import com.coolSchool.coolSchool.models.dto.request.CourseSubsectionRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CourseSubsectionResponseDTO;
import com.coolSchool.coolSchool.services.CourseSubsectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controller class for handling operations related to course subsections.
@RestController
@RequestMapping("/api/v1/courseSubsections")
public class CourseSubsectionController {
    private final CourseSubsectionService courseSubsectionService;

    public CourseSubsectionController(CourseSubsectionService courseSubsectionService) {
        this.courseSubsectionService = courseSubsectionService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<CourseSubsectionResponseDTO>> getAllCategories() {
        return ResponseEntity.ok(courseSubsectionService.getAllCourseSubsections());
    }

    @GetMapping("/course/{id}") // Retrieves all course subsections by course ID.
    public ResponseEntity<List<CourseSubsectionResponseDTO>> getAllCategoriesByCourseId(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(courseSubsectionService.getAllByCourse(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseSubsectionResponseDTO> getCourseSubsectionById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(courseSubsectionService.getCourseSubsectionById(id));
    }

    @RateLimited
    @PostMapping("/create")
    public ResponseEntity<CourseSubsectionResponseDTO> createCourseSubsection(@Valid @RequestBody CourseSubsectionRequestDTO courseSubsectionDTO) {
        CourseSubsectionResponseDTO cratedCourseSubsection = courseSubsectionService.createCourseSubsection(courseSubsectionDTO);
        return new ResponseEntity<>(cratedCourseSubsection, HttpStatus.CREATED);
    }

    @RateLimited
    @PutMapping("/{id}")
    public ResponseEntity<CourseSubsectionResponseDTO> updateCourseSubsection(@PathVariable("id") Long id, @Valid @RequestBody CourseSubsectionRequestDTO courseSubsectionDTO) {
        return ResponseEntity.ok(courseSubsectionService.updateCourseSubsection(id, courseSubsectionDTO));
    }

    @RateLimited
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourseSubsectionById(@PathVariable("id") Long id) {
        courseSubsectionService.deleteCourseSubsection(id);
        return ResponseEntity.ok("CourseSubsection with id: " + id + " has been deleted successfully!");
    }
}
