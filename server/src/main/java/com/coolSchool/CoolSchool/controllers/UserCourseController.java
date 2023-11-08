package com.coolSchool.CoolSchool.controllers;

import com.coolSchool.CoolSchool.models.dto.request.UserCourseRequestDTO;
import com.coolSchool.CoolSchool.models.dto.response.UserCourseResponseDTO;
import com.coolSchool.CoolSchool.services.UserCourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/create")
    public ResponseEntity<UserCourseResponseDTO> createUserCourse(@Valid @RequestBody UserCourseRequestDTO userCourseDTO) {
        UserCourseResponseDTO cratedUserCourse = userCourseService.createUserCourse(userCourseDTO);
        return new ResponseEntity<>(cratedUserCourse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserCourseResponseDTO> updateUserCourse(@PathVariable("id") Long id, @Valid @RequestBody UserCourseRequestDTO userCourseDTO) {
        return ResponseEntity.ok(userCourseService.updateUserCourse(id, userCourseDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserCourseById(@PathVariable("id") Long id) {
        userCourseService.deleteUserCourse(id);
        return ResponseEntity.ok("UserCourse with id: " + id + " has been deleted successfully!");
    }
}
