package com.coolSchool.CoolSchool.controllers;

import com.coolSchool.CoolSchool.filters.JwtAuthenticationFilter;
import com.coolSchool.CoolSchool.models.dto.CourseDTO;
import com.coolSchool.CoolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.CoolSchool.services.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CourseDTO> createCourse(@Valid @RequestBody CourseDTO courseDTO, HttpServletRequest httpServletRequest) {
        CourseDTO cratedCourse = courseService.createCourse(courseDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return new ResponseEntity<>(cratedCourse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable("id") Long id, @Valid @RequestBody CourseDTO courseDTO, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourseById(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        courseService.deleteCourse(id, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return ResponseEntity.ok("Course with id: " + id + " has been deleted successfully!");
    }
}
