package com.coolSchool.CoolSchool.controllers;

import com.coolSchool.CoolSchool.models.dto.CourseSubsectionDTO;
import com.coolSchool.CoolSchool.services.CourseSubsectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courseSubsections")
public class CourseSubsectionController {
    private final CourseSubsectionService courseSubsectionService;

    public CourseSubsectionController(CourseSubsectionService courseSubsectionService) {
        this.courseSubsectionService = courseSubsectionService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<CourseSubsectionDTO>> getAllCategories() {
        return ResponseEntity.ok(courseSubsectionService.getAllCourseSubsections());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseSubsectionDTO> getCourseSubsectionById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(courseSubsectionService.getCourseSubsectionById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CourseSubsectionDTO> createCourseSubsection(@Valid @RequestBody CourseSubsectionDTO courseSubsectionDTO) {
        CourseSubsectionDTO cratedCourseSubsection = courseSubsectionService.createCourseSubsection(courseSubsectionDTO);
        return new ResponseEntity<>(cratedCourseSubsection, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseSubsectionDTO> updateCourseSubsection(@PathVariable("id") Long id, @Valid @RequestBody CourseSubsectionDTO courseSubsectionDTO) {
        return ResponseEntity.ok(courseSubsectionService.updateCourseSubsection(id, courseSubsectionDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourseSubsectionById(@PathVariable("id") Long id) {
        courseSubsectionService.deleteCourseSubsection(id);
        return ResponseEntity.ok("CourseSubsection with id: " + id + " has been deleted successfully!");
    }
}
