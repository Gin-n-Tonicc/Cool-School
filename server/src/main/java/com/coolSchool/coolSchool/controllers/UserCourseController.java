package com.coolSchool.coolSchool.controllers;

import com.coolSchool.coolSchool.interfaces.RateLimited;
import com.coolSchool.coolSchool.models.dto.request.UserCourseRequestDTO;
import com.coolSchool.coolSchool.models.dto.response.UserCourseResponseDTO;
import com.coolSchool.coolSchool.services.UserCourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A controller class for handling user-course-related operations.
 * Many-to-many relation between the users and courses.
 */
@RestController
@RequestMapping("/api/v1/userCourses")
public class UserCourseController {
    private final UserCourseService userCourseService;

    public UserCourseController(UserCourseService userCourseService) {
        this.userCourseService = userCourseService;
    }


    @GetMapping("/all")
    public ResponseEntity<List<UserCourseResponseDTO>> getAllUserCourses() {
        return ResponseEntity.ok(userCourseService.getAllUserCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserCourseResponseDTO> getUserCourseById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(userCourseService.getUserCourseById(id));
    }

    @RateLimited
    @PostMapping("/create")
    public ResponseEntity<UserCourseResponseDTO> createUserCourse(@Valid @RequestBody UserCourseRequestDTO userCourseDTO) {
        UserCourseResponseDTO cratedUserCourse = userCourseService.createUserCourse(userCourseDTO);
        return new ResponseEntity<>(cratedUserCourse, HttpStatus.CREATED);
    }

    @RateLimited
    @PutMapping("/{id}")
    public ResponseEntity<UserCourseResponseDTO> updateUserCourse(@PathVariable("id") Long id, @Valid @RequestBody UserCourseRequestDTO userCourseDTO) {
        return ResponseEntity.ok(userCourseService.updateUserCourse(id, userCourseDTO));
    }

    @RateLimited
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserCourseById(@PathVariable("id") Long id) {
        userCourseService.deleteUserCourse(id);
        return ResponseEntity.ok("UserCourse with id: " + id + " has been deleted successfully!");
    }
}
