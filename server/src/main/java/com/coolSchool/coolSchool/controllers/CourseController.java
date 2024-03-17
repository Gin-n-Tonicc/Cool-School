package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.exceptions.answer.filters.JwtAuthenticationFilter;
import com.coolSchool.coolSchool.interfaces.RateLimited;
import com.coolSchool.coolSchool.models.dto.auth.PublicUserDTO;
import com.coolSchool.coolSchool.models.dto.request.CourseRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.CourseResponseDTO;
import com.coolSchool.coolSchool.services.CourseService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// A controller class for handling course-related operations.
@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<CourseResponseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> getCourseById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/canEnroll/{id}")
    public ResponseEntity<Boolean> canEnroll(@PathVariable(name = "id") Long id, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(courseService.canEnrollCourse(id, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @GetMapping("/enroll/{id}") // Checks if the current user can enroll the course
    public ResponseEntity<Void> enrollCourse(@PathVariable(name = "id") Long id, HttpServletRequest httpServletRequest) {
        courseService.enrollCourse(id, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return ResponseEntity.ok().build();
    }

    @RateLimited
    @PostMapping("/create")
    public ResponseEntity<CourseResponseDTO> createCourse(@Valid @RequestBody CourseRequestDTO courseDTO, HttpServletRequest httpServletRequest) {
        CourseResponseDTO cratedCourse = courseService.createCourse(courseDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return new ResponseEntity<>(cratedCourse, HttpStatus.CREATED);
    }

    @RateLimited
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable("id") Long id, @Valid @RequestBody CourseRequestDTO courseDTO, HttpServletRequest httpServletRequest) {
        return ResponseEntity.ok(courseService.updateCourse(id, courseDTO, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey)));
    }

    @RateLimited
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourseById(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
        courseService.deleteCourse(id, (PublicUserDTO) httpServletRequest.getAttribute(JwtAuthenticationFilter.userKey));
        return ResponseEntity.ok("Course with id: " + id + " has been deleted successfully!");
    }
}
